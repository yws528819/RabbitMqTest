package com.yws.seven;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.yws.utils.MqUtils;

/**
 * 声明主题交换机及相关队列
 * 消费者C1
 */
public class ReceiveLogTopic01 {

    private static final String EXCHANGE_NAME = "topic_logs";

    private static final String QUEUE_NAME = "Q1";

    public static void main(String[] args) throws Exception{

        Channel channel = MqUtils.getChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //队列绑定交换机及消息键
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "*.orange.*");

        System.out.println("等待接收消息...");

        //接收消息
        channel.basicConsume(QUEUE_NAME, true, (consumerTag, message) -> {
            System.out.println("接收队列" + QUEUE_NAME + "绑定键：" + message.getEnvelope().getRoutingKey() + "，收到消息：" + new String(message.getBody()));
        }, consumerTag -> {});

    }
}
