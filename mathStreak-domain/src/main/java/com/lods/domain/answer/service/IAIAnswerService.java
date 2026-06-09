package com.lods.domain.answer.service;

import com.lods.domain.answer.model.entity.AIAnswerMsgEntity;
import com.lods.domain.answer.model.entity.AIAnswerReqEntity;

public interface IAIAnswerService {

    AIAnswerMsgEntity newGenerate(AIAnswerReqEntity aiAnswerReqEntity);
}
