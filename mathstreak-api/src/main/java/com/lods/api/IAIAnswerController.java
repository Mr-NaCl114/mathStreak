package com.lods.api;

import com.lods.api.dto.AIAnswerReqInfo;
import com.lods.api.response.AIAnswerMsgRes;
import com.lods.api.response.Response;

public interface IAIAnswerController {

    Response<AIAnswerMsgRes> Generate(AIAnswerReqInfo info) throws Exception;

    Response<AIAnswerMsgRes> newGenerate() throws Exception;
}
