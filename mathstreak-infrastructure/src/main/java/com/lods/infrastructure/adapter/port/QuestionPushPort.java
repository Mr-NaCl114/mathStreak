package com.lods.infrastructure.adapter.port;

import com.lods.domain.question.adapter.port.IQuestionPushPort;
import com.lods.domain.question.adapter.repository.IQuestionRepository;
import com.lods.domain.question.model.entity.QuestionSubmitEntity;
import com.lods.infrastructure.event.EventPublisher;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class QuestionPushPort implements IQuestionPushPort {

    @Resource
    private EventPublisher eventPublisher;

    @Value("${spring.rabbitmq.config.producer.topic_submit.routing_key}")
    private String routingKey;

    @Override
    public void failTimesPush(QuestionSubmitEntity questionSubmitEntity) {

        eventPublisher.publish(routingKey, questionSubmitEntity);
    }

    @Override
    public void interruptTimesPush(QuestionSubmitEntity questionSubmitEntity) {

        eventPublisher.publish(routingKey, questionSubmitEntity);
    }
}
