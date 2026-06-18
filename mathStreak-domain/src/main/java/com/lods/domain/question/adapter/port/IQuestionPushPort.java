package com.lods.domain.question.adapter.port;

import com.lods.domain.question.model.entity.QuestionSubmitEntity;

public interface IQuestionPushPort {

    void failTimesPush(QuestionSubmitEntity questionSubmitEntity);

    void interruptTimesPush(QuestionSubmitEntity questionSubmitEntity);
}
