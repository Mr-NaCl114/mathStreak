package com.lods.domain.answer.service;

import com.lods.domain.answer.model.entity.AIAnswerGetQuestionReqEntity;
import com.lods.domain.answer.model.entity.AIAnswerMsgEntity;

public interface IAIAnswerService {

    AIAnswerMsgEntity newGenerate(AIAnswerGetQuestionReqEntity aiAnswerGetQuestionReqEntity);

    void newGenerateForAll();

    AIAnswerMsgEntity Generate(AIAnswerGetQuestionReqEntity aiAnswerGetQuestionReqEntity);
}
