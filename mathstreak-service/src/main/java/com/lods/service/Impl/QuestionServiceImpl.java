package com.lods.service.Impl;

import com.lods.common.constants.Constants;
import com.lods.dao.GapQuestionDao;
import com.lods.service.handler.LodsWebSocketHandler;
import com.lods.dao.ChoiceQuestionDao;
import com.lods.domain.dto.SubmitDTO;
import com.lods.domain.po.Question;
import com.lods.domain.res.CheckRes;
import com.lods.domain.res.GameStateRes;
import com.lods.domain.res.QuestionRes;
import com.lods.common.util.ParseInt;
import com.lods.service.QuestionService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
public class QuestionServiceImpl implements QuestionService {

    ParseInt parseInt = new ParseInt();
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private ChoiceQuestionDao choiceQuestionDao;
    @Resource
    private GapQuestionDao gapQuestionDao;
    @Resource
    private LodsWebSocketHandler lodsWebSocketHandler;
    @Resource
    private StreakCount streakCount;

    @Override
    public QuestionRes getQuestion() throws Exception {
        // socket推送
        GameStateRes res = GameStateRes.builder()
                .totalStreak(parseInt.parseInt(stringRedisTemplate.opsForValue().get(Constants.WebStatus.TOTAL_STREAK.getValue())))
                .maxStreak(parseInt.parseInt(stringRedisTemplate.opsForValue().get(Constants.WebStatus.MAX_STREAK.getValue())))
                .life(parseInt.parseInt(stringRedisTemplate.opsForValue().get(Constants.WebStatus.LIFE.getValue())))
                .maxLife(parseInt.parseInt(stringRedisTemplate.opsForValue().get(Constants.WebStatus.MAX_LIFE.getValue())))
                .ipLimit(parseInt.parseInt(stringRedisTemplate.opsForValue().get(Constants.WebStatus.IP_LIMIT.getValue())))
                .build();
        lodsWebSocketHandler.sendMessage(res);

        Question questions = new Question();

        Constants.TypeOfQuestion[] values = Constants.TypeOfQuestion.values();
        Constants.TypeOfQuestion randomChoice = values[ThreadLocalRandom.current().nextInt(values.length)];
        if (randomChoice == Constants.TypeOfQuestion.CHOICE) {
            Integer total = choiceQuestionDao.getTotal().getTotal();
            int random = ThreadLocalRandom.current().nextInt(total + 1);
            questions = choiceQuestionDao.getQuestions(random);
            log.info("当前总计选择题： {} ，抽取 选择题 题号： {}", total, random);
        } else if (randomChoice == Constants.TypeOfQuestion.GAP) {
            Integer total = gapQuestionDao.getTotal().getTotal();
            int random = ThreadLocalRandom.current().nextInt(total + 1);
            questions = gapQuestionDao.getQuestions(random);
            log.info("当前总计填空题： {} ，抽取 填空题 题号： {}", total, random);
        }


        return QuestionRes.builder()
                .type(questions.getOptA() == null ? Constants.TypeOfQuestion.GAP.getCode() : Constants.TypeOfQuestion.CHOICE.getCode())
                .questionId(questions.getQuestionId())
                .description(questions.getDescription())
                .optA(questions.getOptA())
                .optB(questions.getOptB())
                .optC(questions.getOptC())
                .optD(questions.getOptD())
                .difficultyLevel(questions.getDifficultyLevel())
                .build();
    }

    @Override
    public CheckRes submit(SubmitDTO submitDTO) {
        // 选择题
        if (submitDTO.getType().equals(Constants.TypeOfQuestion.CHOICE.getCode())) {
            Question questions = choiceQuestionDao.getQuestions(submitDTO.getQuestionId());

            return CheckRes.builder()
                    .correct(streakCount.isCorrect(questions.getAnswer().equals(submitDTO.getAnswerContent())))
                    .correctLatexAnswer(questions.getAnswer())
                    .build();
        }

        // 填空题
        Question questions = gapQuestionDao.getQuestions(submitDTO.getQuestionId());
        try {
            ExprEvaluator util = new ExprEvaluator();
            // 构造表达式：(标准答案) - (用户答案)
            String checkExpr = "(" + questions.getAnswer() + ") - (" + submitDTO.getAnswerContent() + ")";
            // 让引擎化简
            IExpr result = util.eval("Simplify(" + checkExpr + ")");
            // 如果化简结果等于 0，说明等价
            log.info("result: {}，is?: {}", result, result.isZERO());


            return CheckRes.builder()
                    .correct(streakCount.isCorrect(result.isZERO()))
                    .correctLatexAnswer(questions.getAnswer())
                    .build();
        } catch (Exception e) {
            return CheckRes.builder()
                    .correct(streakCount.isCorrect(submitDTO.getAnswerContent().equals(questions.getAnswer())))
                    .correctLatexAnswer(questions.getAnswer())
                    .build();
        }
    }
}