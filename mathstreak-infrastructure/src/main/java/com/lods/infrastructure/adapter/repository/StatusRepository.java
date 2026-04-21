package com.lods.infrastructure.adapter.repository;

import com.lods.api.response.GameStateRes;
import com.lods.domain.status.apadter.repository.IStatusRepository;
import com.lods.domain.status.model.valobj.GameStatusVO;
import com.lods.infrastructure.dao.po.GameStatus;
import com.lods.infrastructure.redis.StatusUpdate;
import com.lods.types.common.constants.Constants;
import com.lods.types.common.util.ParseInt;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class StatusRepository implements IStatusRepository {

    @Resource
    private StatusUpdate statusUpdate;

    @Override
    public GameStatusVO getCurrentStatus() {
        GameStatus status = statusUpdate.getCurrentStatus();

        return GameStatusVO.builder()
                .totalStreak(status.getTotalStreak())
                .maxStreak(status.getMaxStreak())
                .life(status.getLife())
                .maxLife(status.getMaxLife())
                .ipLimit(status.getIpLimit())
                .build();
    }
}

