package com.lods.domain.question.service;

import com.lods.domain.question.model.entity.QuestionCorrectEntity;
import com.lods.domain.question.model.entity.QuestionDataResEntity;
import com.lods.domain.question.model.entity.QuestionSubmitEntity;

public interface IQuestionService {

    QuestionDataResEntity getQuestion() throws Exception;

    QuestionCorrectEntity submit(QuestionSubmitEntity questionSubmitEntity);
}
