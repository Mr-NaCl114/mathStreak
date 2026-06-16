package com.lods.infrastructure.adapter.repository;

import com.lods.domain.answer.adapter.repository.IAIAnswerRepository;
import com.lods.domain.answer.model.entity.AIAnswerGetQuestionReqEntity;
import com.lods.domain.answer.model.entity.AIAnswerInsertEntity;
import com.lods.domain.question.model.valobj.QuestionVO;
import com.lods.infrastructure.dao.IQuestionChoiceDao;
import com.lods.infrastructure.dao.IQuestionGapDao;
import com.lods.infrastructure.dao.po.Question;
import com.lods.infrastructure.redis.AnswerSign;
import com.lods.types.common.constants.Constants;
import com.lods.types.common.enums.ResponseCode;
import com.lods.types.common.exception.AppException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
public class AIAnswerRepository implements IAIAnswerRepository {

    @Resource
    private IQuestionChoiceDao choiceQuestionDao;
    @Resource
    private IQuestionGapDao gapQuestionDao;
    @Resource
    private AnswerSign answerSign;

    @Override
    public QuestionVO getQuestionById(AIAnswerGetQuestionReqEntity aiAnswerGetQuestionReqEntity) {

        Integer id = aiAnswerGetQuestionReqEntity.getQuestionId();
        Integer type = aiAnswerGetQuestionReqEntity.getType();
        Question question;

        if (Objects.equals(type, Constants.TypeOfQuestion.CHOICE.getCode())) {
            question = choiceQuestionDao.queryQuestionById(id);
        } else if (Objects.equals(type, Constants.TypeOfQuestion.GAP.getCode())) {
            question = gapQuestionDao.queryQuestionById(id);
        } else {
            throw new AppException(ResponseCode.QUESTION_NOT_FOUND.getCode(), ResponseCode.QUESTION_NOT_FOUND.getInfo());
        }

        return QuestionVO.builder()
                .description(question.getDescription())
                .optA(question.getOptA())
                .optB(question.getOptB())
                .optC(question.getOptC())
                .optD(question.getOptD())
                .answer(question.getAnswer())
                .build();
    }

    @Override
    public Integer countChoiceQuestions() {
        return choiceQuestionDao.getTotal().getTotal();
    }

    @Override
    public List<QuestionVO> getChoiceQuestions(int offset, int pageSize) {
        List<Question> questions = choiceQuestionDao.getRangeQuestion(offset, pageSize);

        return questions.stream()
                .map(question -> QuestionVO.builder()
                        .questionId(question.getQuestionId())
                        .description(question.getDescription())
                        .optA(question.getOptA())
                        .optB(question.getOptB())
                        .optC(question.getOptC())
                        .optD(question.getOptD())
                        .answer(question.getAnswer())
                        .build())
                .toList();
    }

    @Override
    public Integer countGapQuestions() {
        return gapQuestionDao.getTotal().getTotal();
    }

    @Override
    public List<QuestionVO> getGapQuestions(int offset, int pageSize) {
        List<Question> questions = gapQuestionDao.getRangeQuestion(offset, pageSize);

        return questions.stream()
                .map(question -> QuestionVO.builder()
                        .questionId(question.getQuestionId())
                        .description(question.getDescription())
                        .answer(question.getAnswer())
                        .build())
                .toList();
    }

    @Override
    public void choiceBatchUpdateAIAnswer(List<AIAnswerInsertEntity> list) {
        choiceQuestionDao.batchUpdateAIAnswer(list);
    }

    @Override
    public void gapBatchUpdateAIAnswer(List<AIAnswerInsertEntity> list) {
        gapQuestionDao.batchUpdateAIAnswer(list);
    }

    @Override
    public String getAnswerByQuestionId(AIAnswerGetQuestionReqEntity aiAnswerGetQuestionReqEntity) {

        if (aiAnswerGetQuestionReqEntity.getType().equals(Constants.TypeOfQuestion.CHOICE.getCode())) {
            return choiceQuestionDao.getAnswerByQuestionId(aiAnswerGetQuestionReqEntity.getQuestionId());
        } else if (aiAnswerGetQuestionReqEntity.getType().equals(Constants.TypeOfQuestion.GAP.getCode())) {
            return gapQuestionDao.getAnswerByQuestionId(aiAnswerGetQuestionReqEntity.getQuestionId());
        }

        return null;
    }

    @Override
    public boolean getAnswerSign(String sign) {

        return answerSign.readSign(sign);
    }
}
