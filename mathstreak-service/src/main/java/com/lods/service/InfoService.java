package com.lods.service;

import com.lods.domain.res.GameStateRes;
import com.lods.domain.vo.QuestionVO;

public interface InfoService {
    GameStateRes getGameState();

    QuestionVO getQuestion();
}
