package com.lods.domain.status.apadter.repository;

import com.lods.domain.status.model.entity.CurrentAnswerChangeEntity;
import com.lods.domain.status.model.entity.QuestionDescriptionEntity;
import com.lods.domain.status.model.valobj.FailInterruptVO;
import com.lods.domain.status.model.valobj.GameStatusVO;
import com.lods.types.common.constants.Constants;

import java.util.List;

public interface IStatusRepository {

    GameStatusVO getCurrentStatus();

    void updateCurrentAnswer(CurrentAnswerChangeEntity currentAnswerChangeEntity);

    void initCurrentAnswer();

    int getRemainingCount();

    void resetRemainCount();

    Constants.FailResult updateStreakCountByIsCorrect(boolean isCorrect);

    void updateFailTimes(QuestionDescriptionEntity questionDescriptionEntity);

    void updateInterruptTimes(QuestionDescriptionEntity questionDescriptionEntity);

    List<FailInterruptVO> getFailInterrupt();

    List<FailInterruptVO> getFailInterruptIncrement(QuestionDescriptionEntity questionDescriptionEntity);
}
