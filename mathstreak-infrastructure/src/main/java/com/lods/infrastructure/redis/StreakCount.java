package com.lods.infrastructure.redis;

import com.lods.types.common.constants.Constants;
import com.lods.types.common.enums.ResponseCode;
import com.lods.types.common.exception.AppException;
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

    public void isCorrect(boolean isCorrect) {
        // 构建 KEYS 集合
        List<String> keys = Arrays.asList(
                Constants.WebStatus.LIFE.getValue(),
                Constants.WebStatus.MAX_LIFE.getValue(),
                Constants.WebStatus.TOTAL_STREAK.getValue(),
                Constants.WebStatus.MAX_STREAK.getValue(),
                Constants.WebStatus.REMAIN_COUNT.getValue()
        );

        // 执行 Lua 脚本，传入 1 表示正确，0 表示错误
        String arg = isCorrect ? "1" : "0";
        Long result = stringRedisTemplate.execute(streakScript, keys, arg);

        // result == -2 表示 accountTodayRemainingCount 为 0（无剩余次数），操作被阻止
        if (result != null && result == -2L) {
            throw new AppException(ResponseCode.REMAIN_COUNT_ZERO.getCode(), ResponseCode.REMAIN_COUNT_ZERO.getInfo());
        } else if (result == 1) {
            
        }
    }

    // 这里是 Lua 脚本内容
    private String getLuaScript() {
        return """
                local is_correct = tonumber(ARGV[1])
                local life_key = KEYS[1]
                local max_life_key = KEYS[2]
                local total_streak_key = KEYS[3]
                local max_streak_key = KEYS[4]
                local remaining_count_key = KEYS[5]
                
                -- 检查 accountTodayRemainingCount 是否为 0，为 0 则退出，不为 0 则 -1 后继续
                local remaining_count = tonumber(redis.call('GET', remaining_count_key) or '0')
                if remaining_count == 0 then
                    return -2
                end
                redis.call('DECR', remaining_count_key)
                
                local current_life = tonumber(redis.call('GET', life_key) or '0')
                local max_life = tonumber(redis.call('GET', max_life_key) or '0')
                local max_streak = tonumber(redis.call('GET', max_streak_key) or '0')
                
                if is_correct == 1 then
                    -- true时：life值+1，不超过上限maxLife
                    if current_life < max_life then
                        redis.call('INCR', life_key)
                    end
                
                    -- totalStreak值+1，超过maxStreak时赋值给maxStreak
                    local new_streak = redis.call('INCR', total_streak_key)
                    if new_streak > max_streak then
                        redis.call('SET', max_streak_key, new_streak)
                    end
                
                    return 1
                else
                    -- false时：life值-1，为0时重置为maxLife和totalStreak重置为0
                    if current_life - 1 <= 0 then
                        redis.call('SET', life_key, max_life)
                        redis.call('SET', total_streak_key, '0')
                    else
                        redis.call('DECR', life_key)
                    end
                
                    return 0
                end
                """;
    }
}
