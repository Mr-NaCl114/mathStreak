package com.lods.service.Impl;

import com.lods.domain.res.GameStateRes;
import com.lods.domain.vo.QuestionVO;
import com.lods.service.InfoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InfoServiceImpl implements InfoService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public GameStateRes getGameState() {
        log.info("获取当前基本状态");
        return GameStateRes.builder()
                .totalStreak(parseInt(stringRedisTemplate.opsForValue().get("totalStreak")))
                .maxStreak(parseInt(stringRedisTemplate.opsForValue().get("maxStreak")))
                .life(parseInt(stringRedisTemplate.opsForValue().get("life")))
                .maxLife(parseInt(stringRedisTemplate.opsForValue().get("maxLife")))
                .ipLimit(parseInt(stringRedisTemplate.opsForValue().get("ipLimit")))
                .build();
    }

    @Override
    public QuestionVO getQuestion() {
        return null;
    }

    private Integer parseInt(String value) {
        return value == null ? null : Integer.valueOf(value);
    }
}
