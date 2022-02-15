package com.yws.two;

import com.rabbitmq.client.Channel;
import com.yws.utils.MqUtils;

public class Worker01 {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception{
        Channel channel = MqUtils.getChannel();

        System.out.println("C2等待接收消息......");
        channel.basicConsume(QUEUE_NAME, true,
                (consumerTag, message) -> {
                    System.out.println("接收到的消息：" + new String(message.getBody()));
                },
                (consumerTag)-> {
                    System.out.println(consumerTag + "消息消费者取消消费接口回调逻辑");
                });
    }
}
