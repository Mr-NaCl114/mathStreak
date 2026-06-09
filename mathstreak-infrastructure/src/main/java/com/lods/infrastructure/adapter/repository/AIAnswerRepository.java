package com.lods.infrastructure.adapter.repository;

import com.lods.domain.answer.adapter.repository.IAIAnswerRepository;
import com.lods.domain.answer.model.entity.AIAnswerReqEntity;
import com.lods.domain.question.model.valobj.QuestionVO;
import com.lods.infrastructure.dao.IQuestionChoiceDao;
import com.lods.infrastructure.dao.IQuestionGapDao;
import com.lods.infrastructure.dao.po.Question;
import com.lods.types.common.constants.Constants;
import com.lods.types.common.enums.ResponseCode;
import com.lods.types.common.exception.AppException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Slf4j
@Repository
public class AIAnswerRepository implements IAIAnswerRepository {

    @Resource
    private IQuestionChoiceDao choiceQuestionDao;
    @Resource
    private IQuestionGapDao gapQuestionDao;

    @Override
    public QuestionVO getQuestionById(AIAnswerReqEntity aiAnswerReqEntity) {

        Integer id = aiAnswerReqEntity.getQuestionId();
        Integer type = aiAnswerReqEntity.getType();
        Question question;

        if(Objects.equals(type, Constants.TypeOfQuestion.CHOICE.getCode())){
            question = choiceQuestionDao.queryQuestionById(id);
        } else if(Objects.equals(type, Constants.TypeOfQuestion.GAP.getCode())){
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
}
