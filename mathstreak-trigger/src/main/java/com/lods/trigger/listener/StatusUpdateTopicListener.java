package com.lods.trigger.listener;

import com.lods.domain.status.model.entity.QuestionDescriptionEntity;
import com.lods.domain.status.service.IStatusService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StatusUpdateTopicListener {

    @Resource
    private IStatusService statusService;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "${spring.rabbitmq.config.producer.topic_fail_submit.queue}"),
                    exchange = @Exchange(value = "${spring.rabbitmq.config.producer.exchange}", type = ExchangeTypes.TOPIC),
                    key = "${spring.rabbitmq.config.producer.topic_fail_submit.routing_key}"
            )
    )
    public void failSubmitListener(QuestionDescriptionEntity message) {

        log.info("failSubmitListener 接收消息 +1");
        statusService.updateFailTimes(message);
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "${spring.rabbitmq.config.producer.topic_interrupt_submit.queue}"),
                    exchange = @Exchange(value = "${spring.rabbitmq.config.producer.exchange}", type = ExchangeTypes.TOPIC),
                    key = "${spring.rabbitmq.config.producer.topic_interrupt_submit.routing_key}"
            )
    )
    public void interruptSubmitListener(QuestionDescriptionEntity message) {

        log.info("interruptSubmitListener 接收消息 +1");
        statusService.updateInterruptTimes(message);
    }

}
