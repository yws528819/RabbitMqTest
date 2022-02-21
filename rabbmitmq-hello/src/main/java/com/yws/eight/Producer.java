package com.yws.eight;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.yws.utils.MqUtils;

/**
 * 死信队列之生产者
 */
public class Producer {

    //普通交换机名称
    private static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception{

        Channel channel = MqUtils.getChannel();

        //死信消息 设置TTL时间 10000ms = 1s
        AMQP.BasicProperties props = new AMQP.BasicProperties()
                .builder().expiration("10000").build();

        for (int i = 1; i < 11; i++) {
            String message = i + "";
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", props, message.getBytes());
        }


    }
}
