package com.lods.domain.status.service;

import com.lods.domain.status.model.entity.CurrentAnswerChangeEntity;
import com.lods.domain.status.model.entity.GameStatusEntity;

public interface IStatusService {
    GameStatusEntity getCurrentStatus();

    void updateCurrentAnswer(CurrentAnswerChangeEntity currentAnswerChangeEntity);

    void initCurrentAnswer();

    int getRemainingCount();

    void resetRemainCount();
}
