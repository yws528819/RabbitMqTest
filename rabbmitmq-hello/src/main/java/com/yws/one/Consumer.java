package com.yws.one;

import com.rabbitmq.client.Channel;
import com.yws.utils.MqUtils;

public class Consumer {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception{
        Channel channel = MqUtils.getChannel();
        //消费消息
        //参数依次为：队列名称、是否自动应答、成功消费消息回调、取消接收消息回调
        channel.basicConsume(QUEUE_NAME, true,
                (consumerTag, message)-> {
                    System.out.println(new String(message.getBody()));
                },
                (consumerTag -> {
                    System.out.println("消息消费被中断");
                }));

    }

}
