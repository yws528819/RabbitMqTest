package com.yws.eight;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.yws.utils.MqUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 死心队列实战
 */
public class Consumer02 {



    //死信队列名称
    private static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception{

        Channel channel = MqUtils.getChannel();
        System.out.println("等待接收消息...");

        //消费消息
        channel.basicConsume(DEAD_QUEUE, true, (consumerTag, message) -> {
            System.out.println("Consumer02接收的消息是：" + new String(message.getBody(), StandardCharsets.UTF_8));
        }, consumerTag -> {});



    }
}
