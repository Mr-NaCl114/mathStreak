package com.lods.domain.status.service.impl;

import com.lods.domain.status.apadter.port.IFailPushPort;
import com.lods.domain.status.apadter.repository.IStatusRepository;
import com.lods.domain.status.model.entity.CurrentAnswerChangeEntity;
import com.lods.domain.status.model.entity.GameStatusEntity;
import com.lods.domain.status.model.entity.QuestionDescriptionCorrectEntity;
import com.lods.domain.status.model.entity.QuestionDescriptionEntity;
import com.lods.domain.status.model.valobj.GameStatusVO;
import com.lods.domain.status.service.IStatusService;
import com.lods.types.common.constants.Constants;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class StatusService implements IStatusService {

    @Resource
    private IStatusRepository statusRepository;
    @Resource
    private IFailPushPort failPushPort;

    @Override
    public GameStatusEntity getCurrentStatus() {

        GameStatusVO status = statusRepository.getCurrentStatus();

        return GameStatusEntity.builder()
                .life(status.getLife())
                .totalStreak(status.getTotalStreak())
                .maxStreak(status.getMaxStreak())
                .maxLife(status.getMaxLife())
                .accountTodayRemainingCount(status.getAccountTodayRemainingCount())
                .answeringCount(status.getAnsweringCount())
                .build();
    }

    @Override
    public void updateCurrentAnswer(CurrentAnswerChangeEntity currentAnswerChangeEntity) {

        statusRepository.updateCurrentAnswer(currentAnswerChangeEntity);
    }

    @Override
    public void initCurrentAnswer() {

        statusRepository.initCurrentAnswer();
    }

    @Override
    public int getRemainingCount() {

        return statusRepository.getRemainingCount();
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    public void resetRemainCount() {

        log.info("重置每日计数");
        statusRepository.resetRemainCount();
    }

    @Override
    public void updateStreakCountByIsCorrect(QuestionDescriptionCorrectEntity questionDescriptionCorrectEntity) {

        //  判题和结果
        Boolean isCorrect = questionDescriptionCorrectEntity.getIsCorrect();
        Constants.FailResult res = statusRepository.updateStreakCountByIsCorrect(isCorrect);

        QuestionDescriptionEntity description = QuestionDescriptionEntity.builder()
                .type(questionDescriptionCorrectEntity.getType())
                .questionId(questionDescriptionCorrectEntity.getQuestionId())
                .build();


        if (res == null) return;
        CompletableFuture.runAsync(() -> {
            if (res == Constants.FailResult.FAIL_THIS) {
                failPushPort.failTimesPush(description);
            } else if (res == Constants.FailResult.INTERRUPTED) {
                failPushPort.interruptTimesPush(description);
            }
        });
    }

    @Override
    public void updateFailTimes(QuestionDescriptionEntity questionDescriptionEntity) {

        statusRepository.updateFailTimes(questionDescriptionEntity);
    }

    @Override
    public void updateInterruptTimes(QuestionDescriptionEntity questionDescriptionEntity) {

        statusRepository.updateInterruptTimes(questionDescriptionEntity);
    }
}
