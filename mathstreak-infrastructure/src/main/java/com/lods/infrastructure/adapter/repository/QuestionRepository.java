package com.lods.infrastructure.adapter.repository;

import com.lods.domain.question.adapter.repository.IQuestionRepository;
import com.lods.domain.question.model.valobj.QuestionVO;
import com.lods.infrastructure.dao.IQuestionChoiceDao;
import com.lods.infrastructure.dao.IQuestionGapDao;
import com.lods.infrastructure.dao.po.Question;
import com.lods.infrastructure.redis.StreakCount;
import com.lods.types.common.constants.Constants;
import com.lods.types.common.enums.ResponseCode;
import com.lods.types.common.exception.AppException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Repository
public class QuestionRepository implements IQuestionRepository {

    @Resource
    private IQuestionChoiceDao choiceQuestionDao;
    @Resource
    private IQuestionGapDao gapQuestionDao;
    @Resource
    private StreakCount streakCount;

    @Override
    public QuestionVO getRandomQuestion() {
        Question question = new Question();
        Constants.TypeOfQuestion[] values = Constants.TypeOfQuestion.values();
        Constants.TypeOfQuestion randomChoice = values[ThreadLocalRandom.current().nextInt(values.length)];

        if (randomChoice == Constants.TypeOfQuestion.CHOICE) {
            Integer total = choiceQuestionDao.getTotal().getTotal();
            int random = ThreadLocalRandom.current().nextInt(1, total + 1);
            question = choiceQuestionDao.getCurrentQuestion(random);
            log.info("当前总计选择题： {} ，抽取 选择题 题号： {}", total, random);
        } else if (randomChoice == Constants.TypeOfQuestion.GAP) {
            Integer total = gapQuestionDao.getTotal().getTotal();
            int random = ThreadLocalRandom.current().nextInt(1, total + 1);
            question = gapQuestionDao.getCurrentQuestion(random);
            log.info("当前总计填空题： {} ，抽取 填空题 题号： {}", total, random);
        }

        return QuestionVO.builder()
                .questionId(question.getQuestionId())
                .description(question.getDescription())
                .optA(question.getOptA())
                .optB(question.getOptB())
                .optC(question.getOptC())
                .optD(question.getOptD())
                .answer(question.getAnswer())
                .difficultyLevel(question.getDifficultyLevel())
                .total(question.getTotal())
                .build();
    }

    @Override
    public QuestionVO getQuestionChoiceById(int Id) {
        Question question = choiceQuestionDao.queryQuestionById(Id);

        if(question == null){
            throw new AppException(ResponseCode.QUESTION_NOT_FOUND.getCode(), ResponseCode.QUESTION_NOT_FOUND.getInfo());
        }

        return QuestionVO.builder()
                .questionId(question.getQuestionId())
                .description(question.getDescription())
                .optA(question.getOptA())
                .optB(question.getOptB())
                .optC(question.getOptC())
                .optD(question.getOptD())
                .answer(question.getAnswer())
                .difficultyLevel(question.getDifficultyLevel())
                .total(question.getTotal())
                .build();

    }

    @Override
    public QuestionVO getQuestionGapById(int Id) {
        Question question = gapQuestionDao.queryQuestionById(Id);

        if(question == null){
            throw new AppException(ResponseCode.QUESTION_NOT_FOUND.getCode(), ResponseCode.QUESTION_NOT_FOUND.getInfo());
        }

        return QuestionVO.builder()
                .questionId(question.getQuestionId())
                .description(question.getDescription())
                .answer(question.getAnswer())
                .difficultyLevel(question.getDifficultyLevel())
                .total(question.getTotal())
                .build();
    }

    @Override
    public void updateStreakCountByIsCorrect(boolean isCorrect) {
        streakCount.isCorrect(isCorrect);
    }
}
