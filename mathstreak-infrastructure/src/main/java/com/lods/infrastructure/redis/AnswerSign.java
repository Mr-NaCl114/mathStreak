package com.lods.infrastructure.redis;

import com.lods.types.common.constants.Constants;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;

@Component
public class AnswerSign {

    private static final Duration SIGN_TTL = Duration.ofHours(24);
    private static final String LUA_READ_SIGN = """
            local key = KEYS[1]
            if redis.call('GET', key) then
                redis.call('DEL', key)
                return 1
            else
                return 0
            end
            """;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public String writeSign() {
        String uuid = UUID.randomUUID().toString();
        String key = Constants.AnswerSignKey.SIGN_PREFIX.getValue() + uuid;
        stringRedisTemplate.opsForValue().set(key, "1", SIGN_TTL);
        return uuid;
    }

    public boolean readSign(String answerSign) {
        String key = Constants.AnswerSignKey.SIGN_PREFIX.getValue() + answerSign;
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(LUA_READ_SIGN, Long.class);
        Long result = stringRedisTemplate.execute(redisScript,
                Collections.singletonList(key));
        return result != null && result == 1;
    }
}
