package com.lods.infrastructure.adapter.port;

import com.lods.domain.status.apadter.port.IFailPushPort;
import com.lods.domain.status.model.entity.QuestionDescriptionEntity;
import com.lods.infrastructure.event.EventPublisher;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FailPushPort implements IFailPushPort {

    @Resource
    private EventPublisher eventPublisher;

    @Value("${spring.rabbitmq.config.producer.topic_fail_submit.routing_key}")
    private String failTimesPushRoutingKey;
    @Value("${spring.rabbitmq.config.producer.topic_interrupt_submit.routing_key}")
    private String interruptTimesPushRoutingKey;

    @Override
    public void failTimesPush(QuestionDescriptionEntity questionDescriptionEntity) {

        eventPublisher.publish(failTimesPushRoutingKey, questionDescriptionEntity);
    }

    @Override
    public void interruptTimesPush(QuestionDescriptionEntity questionDescriptionEntity) {

        eventPublisher.publish(interruptTimesPushRoutingKey, questionDescriptionEntity);
    }
}
