package com.lods.service.Impl;

import com.lods.dao.ChoiceQuestionDao;
import com.lods.domain.res.GameStateRes;
import com.lods.domain.vo.ChoiceQuestionVO;
import com.lods.service.InfoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
public class InfoServiceImpl implements InfoService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private ChoiceQuestionDao choiceQuestionDao;

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
    public ChoiceQuestionVO getQuestion() {
        //TODO 添加填空

        Integer total = choiceQuestionDao.getTotal().getTotal();
        int random = ThreadLocalRandom.current().nextInt(total + 1);
        log.info("当前总计选择题： {} ，抽取选择题题号： {}", total, random);

        log.info("{}",choiceQuestionDao.getQuestions(total));

        return choiceQuestionDao.getQuestions(total);
    }

    private Integer parseInt(String value) {
        return value == null ? null : Integer.valueOf(value);
    }
}
