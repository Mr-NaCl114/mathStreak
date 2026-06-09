package com.lods.domain.answer.adapter.repository;

import com.lods.domain.answer.model.entity.AIAnswerReqEntity;
import com.lods.domain.question.model.valobj.QuestionVO;

public interface IAIAnswerRepository {

    QuestionVO getQuestionById(AIAnswerReqEntity aiAnswerReqEntity);

}
