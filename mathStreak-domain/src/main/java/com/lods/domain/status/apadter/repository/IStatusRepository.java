package com.lods.domain.status.apadter.repository;

import com.lods.domain.status.model.entity.CurrentAnswerChangeEntity;
import com.lods.domain.status.model.valobj.GameStatusVO;

public interface IStatusRepository {

    GameStatusVO getCurrentStatus();

    void updateCurrentAnswer(CurrentAnswerChangeEntity currentAnswerChangeEntity);

    void initCurrentAnswer();

    int getRemainingCount();

    void resetRemainCount();
}
