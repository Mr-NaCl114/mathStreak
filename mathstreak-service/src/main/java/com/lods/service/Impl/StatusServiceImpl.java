package com.lods.service.Impl;

import com.lods.common.constants.Constants;
import com.lods.common.util.ParseInt;
import com.lods.domain.res.GameStateRes;
import com.lods.service.StatusService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class StatusServiceImpl implements StatusService {

    ParseInt parseInt = new ParseInt();
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public GameStateRes updateStatus() {
        return GameStateRes.builder()
                .totalStreak(parseInt.parseInt(stringRedisTemplate.opsForValue().get(Constants.WebStatus.TOTAL_STREAK.getValue())))
                .maxStreak(parseInt.parseInt(stringRedisTemplate.opsForValue().get(Constants.WebStatus.MAX_STREAK.getValue())))
                .life(parseInt.parseInt(stringRedisTemplate.opsForValue().get(Constants.WebStatus.LIFE.getValue())))
                .maxLife(parseInt.parseInt(stringRedisTemplate.opsForValue().get(Constants.WebStatus.MAX_LIFE.getValue())))
                .ipLimit(parseInt.parseInt(stringRedisTemplate.opsForValue().get(Constants.WebStatus.IP_LIMIT.getValue())))
                .build();
    }
}
