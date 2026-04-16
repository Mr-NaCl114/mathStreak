package com.lods.domain.service;

import com.lods.api.dto.SubmitDTO;
import com.lods.api.response.QuestionRes;
import com.lods.api.response.CheckRes;

public interface QuestionService {

    QuestionRes getQuestion() throws Exception;

    CheckRes submit(SubmitDTO submitDTO);
}
