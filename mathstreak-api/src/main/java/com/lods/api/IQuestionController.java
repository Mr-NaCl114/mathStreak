package com.lods.api;

import com.lods.api.dto.SubmitDTO;
import com.lods.api.response.Response;

public interface IQuestionController {

    Response<Object> currentQuestion() throws Exception;

    Response<Object> submit(SubmitDTO submitDTO) throws Exception;

    Response<Object> resetRemainCount() throws Exception;
}
