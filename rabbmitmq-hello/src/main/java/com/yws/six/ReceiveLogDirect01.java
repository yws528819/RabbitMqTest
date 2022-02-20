package com.yws.six;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.yws.utils.MqUtils;

import java.util.Queue;

/**
 *
 */
public class ReceiveLogDirect01 {
    //交换机名称
    private static final String EXCHANGE_NAME = "direct_logs";
    //队列名称
    private static final String QUEUE_NAME = "console";

    public static void main(String[] args) throws Exception{
        Channel channel = MqUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //交换机绑定队列
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "info");
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "warning");

        //消费消息
        channel.basicConsume(QUEUE_NAME, true,
                (consumerTag, message) -> {
                    System.out.println("ReceiveLogDirect01控制台打印接收到的消息：" + new String(message.getBody()));
                },
                consumerTag -> {});
    }

}
