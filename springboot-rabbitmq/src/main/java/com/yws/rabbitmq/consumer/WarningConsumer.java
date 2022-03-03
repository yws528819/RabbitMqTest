package com.yws.rabbitmq.consumer;

import com.yws.rabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 *  报警消费者
 */
@Slf4j
@Component
public class WarningConsumer {

    //接收报警的消息
    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE_NAME)
    public void receiveWarning(Message message) {
        String msg = new String(message.getBody());
        log.info("报警发现不可路由的消息:{}", msg);

    }

}
