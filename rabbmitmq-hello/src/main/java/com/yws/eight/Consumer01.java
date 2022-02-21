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
public class Consumer01 {

    //普通交换机名称
    private static final String NORMAL_EXCHANGE = "normal_exchange";
    //死信交换机名称
    private static final String DEAD_EXCHANGE = "dead_exchange";

    //普通队列名称
    private static final String NORMAL_QUEUE = "normal_queue";
    //死信队列名称
    private static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception{

        Channel channel = MqUtils.getChannel();

        //声明交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);


        //声明普通队列
        Map<String, Object> arguments = new HashMap<>();
        //过期时间 10s = 10000ms
        //arguments.put("x-message-ttl", 10000);
        //正常队列设置死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //设置死信Routingkey
        arguments.put("x-dead-letter-routing-key", "lisi");
        channel.queueDeclare(NORMAL_QUEUE, false, false, false, null);

        //声明死信队列
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);


        //绑定普通的交换机-队列
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
        //绑定死信的交换机-队列
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");

        System.out.println("等待接收消息...");

        //消费消息
        channel.basicConsume(NORMAL_QUEUE, true, (consumerTag, message) -> {
            System.out.println("Consumer01接收的消息是：" + new String(message.getBody(), StandardCharsets.UTF_8));
        }, consumerTag -> {});



    }
}
