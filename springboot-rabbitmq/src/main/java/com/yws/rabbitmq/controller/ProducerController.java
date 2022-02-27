package com.yws.rabbitmq.controller;

import com.yws.rabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 开始发消息，测试确认
 */
@Slf4j
@RequestMapping("/confirm")
@RestController
public class ProducerController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendmessage/{message}")
    public void sendMessage(@PathVariable("message") String message) {
        //场景1 发送到一个不存在的交换机

        //场景2 发送到交换机，消息键找不到队列，不可路由

        CorrelationData correlationData1 = new CorrelationData("1");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME, ConfirmConfig.CONFIRM_ROUTING_KEY, message+ "key1", correlationData1);
        log.info("发送消息内容：{}", message+ "key1");

        CorrelationData correlationData2 = new CorrelationData("2");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME, ConfirmConfig.CONFIRM_ROUTING_KEY + "2", message + "key12", correlationData2);
        log.info("发送消息内容：{}", message + "key12");


    }
}
