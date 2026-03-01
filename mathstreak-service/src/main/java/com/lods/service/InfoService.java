package com.lods.service;

import com.lods.domain.dto.SubmitDTO;
import com.lods.domain.res.QuestionRes;
import com.lods.domain.res.CheckRes;

public interface InfoService {
//    GameStateRes getGameState();

    QuestionRes getQuestion() throws Exception;

    CheckRes submit(SubmitDTO submitDTO);
}
