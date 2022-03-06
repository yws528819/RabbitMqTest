package com.yws.one;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

public class Producer {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        //连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //IP、用户名、密码
        factory.setHost("192.168.0.105");
        factory.setUsername("admin");
        factory.setPassword("123");
        //创建信道、连接
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //声明队列
        //参数依次为队列名、是否持久化、是否独占、是否自动删除、其他参数
        Map<String, Object> arguments = new HashMap<>();

        //官方允许是0-255之间 此处设置10 允许优先值范围0-10 不要设置过大 浪费CPU与内存
        arguments.put("x-max-priority", 10);
        channel.queueDeclare(QUEUE_NAME, true, false, false, arguments);

        for (int i = 1; i < 11; i++) {
            String message = "info" + i;
            if (i == 5) {
                AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(5).build();
                channel.basicPublish("", QUEUE_NAME, properties, message.getBytes());
            }else {
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

            }
        }

        //发送的消息
        //String message = "hello";
        //channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("消息发送完毕");
    }
}
