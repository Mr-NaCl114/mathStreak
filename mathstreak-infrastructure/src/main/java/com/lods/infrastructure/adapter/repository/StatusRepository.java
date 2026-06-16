package com.lods.infrastructure.adapter.repository;

import com.lods.domain.status.apadter.repository.IStatusRepository;
import com.lods.domain.status.model.entity.CurrentAnswerChangeEntity;
import com.lods.domain.status.model.valobj.GameStatusVO;
import com.lods.infrastructure.dao.po.GameStatus;
import com.lods.infrastructure.redis.StatusOpt;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class StatusRepository implements IStatusRepository {

    @Resource
    private StatusOpt statusOpt;

    @Override
    public GameStatusVO getCurrentStatus() {

        GameStatus status = statusOpt.getCurrentStatus();

        return GameStatusVO.builder()
                .totalStreak(status.getTotalStreak())
                .maxStreak(status.getMaxStreak())
                .life(status.getLife())
                .maxLife(status.getMaxLife())
                .accountTodayRemainingCount(status.getAccountTodayRemainingCount())
                .answeringCount(status.getAnsweringCount())
                .build();
    }

    @Override
    public void updateCurrentAnswer(CurrentAnswerChangeEntity currentAnswerChangeEntity) {

        statusOpt.CurrentAnswerCount(currentAnswerChangeEntity.getIsAdd());
    }

    @Override
    public void initCurrentAnswer() {

        statusOpt.initCurrentAnswer();
    }

    @Override
    public int getRemainingCount() {

        return statusOpt.getRemainingCount();
    }
}

