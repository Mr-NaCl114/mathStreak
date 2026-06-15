package com.lods.domain.status.service.impl;

import com.lods.domain.status.apadter.repository.IStatusRepository;
import com.lods.domain.status.model.entity.CurrentAnswerChangeEntity;
import com.lods.domain.status.model.entity.GameStatusEntity;
import com.lods.domain.status.model.valobj.GameStatusVO;
import com.lods.domain.status.service.IStatusService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class StatusService implements IStatusService {

    @Resource
    private IStatusRepository statusRepository;

    public GameStatusEntity getCurrentStatus() {

        GameStatusVO status = statusRepository.getCurrentStatus();

        return GameStatusEntity.builder()
                .life(status.getLife())
                .totalStreak(status.getTotalStreak())
                .maxStreak(status.getMaxStreak())
                .maxLife(status.getMaxLife())
                .ipLimit(status.getIpLimit())
                .currentQuestionAnsweringCount(status.getCurrentQuestionAnsweringCount())
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
}
