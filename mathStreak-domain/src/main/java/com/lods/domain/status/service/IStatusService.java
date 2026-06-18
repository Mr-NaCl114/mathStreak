package com.lods.domain.status.service;

import com.lods.domain.status.model.entity.CurrentAnswerChangeEntity;
import com.lods.domain.status.model.entity.GameStatusEntity;
import com.lods.domain.status.model.entity.QuestionDescriptionCorrectEntity;

public interface IStatusService {
    GameStatusEntity getCurrentStatus();

    void updateCurrentAnswer(CurrentAnswerChangeEntity currentAnswerChangeEntity);

    void initCurrentAnswer();

    int getRemainingCount();

    void resetRemainCount();

    void updateStreakCountByIsCorrect(QuestionDescriptionCorrectEntity questionDescriptionCorrectEntity);
}
