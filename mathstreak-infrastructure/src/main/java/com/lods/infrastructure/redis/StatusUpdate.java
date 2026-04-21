package com.lods.infrastructure.redis;

import com.lods.infrastructure.dao.po.GameStatus;
import com.lods.types.common.constants.Constants;
import com.lods.types.common.util.ParseInt;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class StatusUpdate {

    ParseInt parseInt = new ParseInt();
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public GameStatus getCurrentStatus() {
        return GameStatus.builder()
                .totalStreak(parseInt.parseInt(stringRedisTemplate.opsForValue().get(Constants.WebStatus.TOTAL_STREAK.getValue())))
                .maxStreak(parseInt.parseInt(stringRedisTemplate.opsForValue().get(Constants.WebStatus.MAX_STREAK.getValue())))
                .life(parseInt.parseInt(stringRedisTemplate.opsForValue().get(Constants.WebStatus.LIFE.getValue())))
                .maxLife(parseInt.parseInt(stringRedisTemplate.opsForValue().get(Constants.WebStatus.MAX_LIFE.getValue())))
                .ipLimit(parseInt.parseInt(stringRedisTemplate.opsForValue().get(Constants.WebStatus.IP_LIMIT.getValue())))
                .build();
    }
}

