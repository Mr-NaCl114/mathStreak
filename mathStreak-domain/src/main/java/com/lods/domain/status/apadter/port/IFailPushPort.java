package com.lods.domain.status.apadter.port;

import com.lods.domain.question.model.entity.QuestionSubmitEntity;
import com.lods.domain.status.model.entity.QuestionDescriptionCorrectEntity;

public interface IFailPushPort {

    void failTimesPush(QuestionDescriptionCorrectEntity questionDescriptionCorrectEntity);

    void interruptTimesPush(QuestionDescriptionCorrectEntity questionDescriptionCorrectEntity);
}
