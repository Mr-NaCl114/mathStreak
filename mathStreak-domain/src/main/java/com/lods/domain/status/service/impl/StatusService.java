package com.lods.domain.status.service.impl;

import com.lods.domain.status.apadter.repository.IStatusRepository;
import com.lods.domain.status.model.entity.CurrentAnswerChangeEntity;
import com.lods.domain.status.model.entity.GameStatusEntity;
import com.lods.domain.status.model.valobj.GameStatusVO;
import com.lods.domain.status.service.IStatusService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StatusService implements IStatusService {

    @Resource
    private IStatusRepository statusRepository;

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
}
