package com.lods.service.Impl;

import com.lods.common.constants.Constants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class StreakCount {

    private final StringRedisTemplate stringRedisTemplate;
    private final DefaultRedisScript<Long> streakScript;

    public StreakCount(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        // 初始化 Lua 脚本
        this.streakScript = new DefaultRedisScript<>();
        this.streakScript.setScriptText(getLuaScript()); // 也可以用 setScriptSource 读文件
        this.streakScript.setResultType(Long.class);
    }

    public Integer isCorrect(boolean isCorrect) {
        // 构建 KEYS 集合
        List<String> keys = Arrays.asList(
                Constants.WebStatus.LIFE.getValue(),
                Constants.WebStatus.MAX_LIFE.getValue(),
                Constants.WebStatus.TOTAL_STREAK.getValue(),
                Constants.WebStatus.MAX_STREAK.getValue()
        );

        // 执行 Lua 脚本，传入 1 表示正确，0 表示错误
        String arg = isCorrect ? "1" : "0";
        Long result = stringRedisTemplate.execute(streakScript, keys, arg);

        // 返回业务状态码
        return result != null && result == 1L
                ? Constants.AnswerRes.RIGHT.getCode()
                : Constants.AnswerRes.FALI.getCode();
    }

    // 这里是 Lua 脚本内容
    private String getLuaScript() {
        return """
            local is_correct = tonumber(ARGV[1])
            local life_key = KEYS[1]
            local max_life_key = KEYS[2]
            local total_streak_key = KEYS[3]
            local max_streak_key = KEYS[4]

            local current_life = tonumber(redis.call('GET', life_key) or '0')
            local max_life = tonumber(redis.call('GET', max_life_key) or '0')
            local total_streak = tonumber(redis.call('GET', total_streak_key) or '0')
            local max_streak = tonumber(redis.call('GET', max_streak_key) or '0')

            if is_correct == 1 then
                if current_life < max_life then
                    redis.call('INCR', life_key)
                end
                redis.call('INCR', total_streak_key)
                return 1 -- 返回 1 表示处理为正确逻辑
            else
                if current_life - 1 <= 0 then
                    -- 生命值归零，重置生命值和连胜
                    redis.call('SET', life_key, max_life)
                    if total_streak > max_streak then
                        redis.call('SET', max_streak_key, total_streak)
                    end
                    redis.call('SET', total_streak_key, '0')
                else
                    -- 扣减生命值
                    redis.call('DECR', life_key)
                end
                return 0 -- 返回 0 表示处理为失败逻辑
            end
            """;
    }
}
