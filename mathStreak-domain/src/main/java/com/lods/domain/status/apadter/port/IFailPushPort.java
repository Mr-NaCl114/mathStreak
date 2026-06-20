package com.lods.domain.status.apadter.port;

import com.lods.domain.status.model.entity.QuestionDescriptionEntity;

public interface IFailPushPort {

    void failTimesPush(QuestionDescriptionEntity questionDescriptionEntity);

    void interruptTimesPush(QuestionDescriptionEntity questionDescriptionEntity);
}
