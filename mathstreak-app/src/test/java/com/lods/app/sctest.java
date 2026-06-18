package com.lods.app;

import com.lods.domain.answer.model.entity.AIAnswerGetQuestionReqEntity;
import com.lods.domain.answer.model.entity.AIAnswerMsgEntity;
import com.lods.domain.answer.service.IAIAnswerService;
import com.lods.domain.question.model.entity.QuestionCorrectEntity;
import com.lods.domain.question.model.entity.QuestionSubmitEntity;
import com.lods.domain.question.service.IQuestionService;
import com.lods.domain.status.service.IStatusService;
import com.lods.infrastructure.event.EventPublisher;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.CountDownLatch;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class sctest {

    @Resource
    private IStatusService statusService;
    @Resource
    private IAIAnswerService aiAnswerService;
    @Resource
    private IQuestionService questionService;
    @Resource
    private EventPublisher publisher;

    @Test
    public void testResetRemainCount() {

        statusService.resetRemainCount();
    }

//    @Test
    public void newGen(){

        QuestionCorrectEntity submit = questionService.submit(QuestionSubmitEntity.builder()
                .type(1)
                .answerContent("123")
                .questionId(12)
                .build());

        AIAnswerMsgEntity res = aiAnswerService.newGenerate(AIAnswerGetQuestionReqEntity.builder()
                .type(1)
                .questionId(22)
                .sign(submit.getSign())
                .build());

        log.info("res:{}", res);
    }

    @Test
    public void test_rabbitmq() throws InterruptedException {
//        CountDownLatch countDownLatch = new CountDownLatch(1);

        publisher.publish("topic.status_update", "测试消息：123");

        // 等待，消息消费。测试后，可主动关闭。
//        countDownLatch.await();
//        countDownLatch.countDown();
//        countDownLatch.countDown();
    }
}
