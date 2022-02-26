package com.yws.rabbitmq.controller;

import com.yws.rabbitmq.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 发送延迟消息
 */
@Slf4j
@RequestMapping("/ttl")
@RestController
public class SendMsgController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息
     * @param message
     */
    @GetMapping("/sendmsg/{message}")
    public void sendMsg(@PathVariable("message")  String message) {
        log.info("当前时间：{}，发送一条信息给两个TTL队列：{}", new Date().toString(), message );
        rabbitTemplate.convertAndSend("X", "XA", "消息来自ttl为10s的队列：" + message);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自ttl为40s的队列：" + message);
    }


    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public void sendMsg(@PathVariable("message") String message, @PathVariable("ttlTime") String ttlTime) {
        log.info("当前时间{}，发送一条时长{}毫秒TTL信息给队列QC：{}", new Date().toString(), ttlTime, message);
        rabbitTemplate.convertAndSend("X", "XC", message, msg -> {
            //发送消息的时候，延迟时长
            msg.getMessageProperties().setExpiration(ttlTime);
            return msg;
        });
    }

    @GetMapping("/sendDelayMsg/{message}/{delayTime}")
    public void sendDelayedMsg(@PathVariable("message") String message, @PathVariable("delayTime") Integer delayTime) {
        log.info("当前时间{}，发送一条延迟时长{}毫秒信息给延迟队列delayed.queue：{}", new Date().toString(), delayTime, message);
        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE, DelayedQueueConfig.DELAYED_ROUTING_KEY, message, msg -> {
            //发送消息的时候 延迟时长 ms
            msg.getMessageProperties().setDelay(delayTime);
            return msg;
        });
    }
}
