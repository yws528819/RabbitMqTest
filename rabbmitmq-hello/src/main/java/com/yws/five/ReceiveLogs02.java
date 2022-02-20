package com.yws.five;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.yws.utils.MqUtils;

/**
 * 消息接收
 */
public class ReceiveLogs02 {
    //交换机名称
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception{
        Channel channel = MqUtils.getChannel();

        //声明一个交换机（在生产者声明和绑定也可以）
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        //声明一个队列 临时
        /**
         * 临时队列，队列名称是随机的
         * 当消费者断开与队列的连接时，队列就自动删除
         */
        String queueName = channel.queueDeclare().getQueue();
        //绑定交换机与队列
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("等待接收消息，把接收到的消息打在屏幕上...");

        //消费消息
        channel.basicConsume(queueName, true,
                (consumerTag, message) -> {
                    System.out.println("ReceiveLogs02控制台打印接收到的消息：" + new String(message.getBody()));
                },
                consumerTag -> {});

    }
}
