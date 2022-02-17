package com.yws.three;

import com.rabbitmq.client.Channel;
import com.yws.utils.MqUtils;

import java.util.concurrent.TimeUnit;

/**
 * 消息在手动应答时是不丢失、放回队列中重新消费
 */
public class Worker04 {
    //队列名称
    private static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = MqUtils.getChannel();

        System.out.println("C2接收消息处理时间较长");

        //采用手动应答
        channel.basicConsume(QUEUE_NAME, false,
                (consumerTag, message) -> {
                    //沉睡1s
                    try {
                        TimeUnit.SECONDS.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("接收到的消息：" + new String(message.getBody()));
                    //手动应答
                    //参数依次为消息的标记tag，是否批量应答
                    channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
                },
                consumerTag -> {});

    }
}
