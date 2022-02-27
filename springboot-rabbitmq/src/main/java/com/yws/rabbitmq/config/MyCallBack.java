package com.yws.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class MyCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        //注入（把当前实现类注入到RabbitTemplate里）
        rabbitTemplate.setConfirmCallback(this);

        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 交换机发布确认回调方法
     * @param correlationData 保存回调消息的ID及相关消息
     * @param ack 交换机是否收到消息
     * @param cause 失败原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String msgId = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机已经收到ID为：{}的消息", msgId);
        }else {
            log.info("交换机未收到ID为：{}的消息，由于原因：{}", msgId, cause);
        }
    }

    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.error("消息{}，被交换机{}退回，退回原因{}，路由key：{}", new String(returned.getMessage().getBody()), returned.getExchange(), returned.getReplyText(), returned.getRoutingKey());
    }
}
