package com.lods.domain.question.service.impl;

import com.lods.domain.question.adapter.repository.IQuestionRepository;
import com.lods.domain.question.model.entity.QuestionCorrectEntity;
import com.lods.domain.question.model.entity.QuestionDataResEntity;
import com.lods.domain.question.model.entity.QuestionSubmitEntity;
import com.lods.domain.question.model.valobj.QuestionVO;
import com.lods.types.common.constants.Constants;
import com.lods.domain.question.service.IQuestionService;
import com.lods.types.common.enums.ResponseCode;
import com.lods.types.common.exception.AppException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class QuestionServiceImpl implements IQuestionService {
    @Resource
    private IQuestionRepository questionRepository;

    @Override
    public QuestionDataResEntity getQuestion() throws Exception {

        QuestionVO question = questionRepository.getRandomQuestion();

        return QuestionDataResEntity.builder()
                .type(question.getOptA() == null ? Constants.TypeOfQuestion.GAP.getCode() : Constants.TypeOfQuestion.CHOICE.getCode())
                .questionId(question.getQuestionId())
                .description(question.getDescription())
                .optA(question.getOptA())
                .optB(question.getOptB())
                .optC(question.getOptC())
                .optD(question.getOptD())
                .difficultyLevel(question.getDifficultyLevel())
                .build();
    }

    @Override
    public QuestionCorrectEntity submit(QuestionSubmitEntity questionSubmitEntity) {
        // 选择题
        if (questionSubmitEntity.getType().equals(Constants.TypeOfQuestion.CHOICE.getCode())) {
            QuestionVO questionVO = questionRepository.getQuestionChoiceById(questionSubmitEntity.getQuestionId());
            boolean res = questionVO.getAnswer().equals(questionSubmitEntity.getAnswerContent());

            //  更新状态
            questionRepository.updateStreakCountByIsCorrect(res);

            return QuestionCorrectEntity.builder()
                    .isCorrect(res)
                    .correctLatexAnswer(questionVO.getAnswer())
                    .build();
        }

        // 填空题
        QuestionVO questions = questionRepository.getQuestionGapById(questionSubmitEntity.getQuestionId());
        try {
            ExprEvaluator util = new ExprEvaluator();
            // 构造表达式：(标准答案) - (用户答案)
            String checkExpr = "(" + questions.getAnswer() + ") - (" + questionSubmitEntity.getAnswerContent() + ")";
            // 让引擎化简
            IExpr result = util.eval("Simplify(" + checkExpr + ")");
            // 如果化简结果等于 0，说明等价
            log.info("result: {}，is?: {}", result, result.isZERO());

            boolean res = result.isZERO();
            //  更新状态
            questionRepository.updateStreakCountByIsCorrect(res);

            return QuestionCorrectEntity.builder()
                    .isCorrect(res)
                    .correctLatexAnswer(questions.getAnswer())
                    .build();
        } catch (Exception e) {
            throw new AppException(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getInfo());
        }
    }
}