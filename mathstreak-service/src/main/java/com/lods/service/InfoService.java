package com.lods.service;

import com.lods.domain.res.GameStateRes;
import com.lods.domain.vo.ChoiceQuestionVO;

public interface InfoService {
    GameStateRes getGameState();

    ChoiceQuestionVO getQuestion();
}
