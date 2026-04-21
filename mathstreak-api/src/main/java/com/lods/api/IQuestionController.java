package com.lods.api;

import com.lods.api.dto.SubmitDTO;
import com.lods.api.response.Response;

import java.io.IOException;

public interface IQuestionController {

    Response<Object> currentQuestion() throws Exception;

    Response<Object> submit(SubmitDTO submitDTO) throws IOException;
}
