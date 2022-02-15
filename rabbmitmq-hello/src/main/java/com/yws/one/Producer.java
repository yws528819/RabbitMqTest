package com.yws.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

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
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //发送的消息
        String message = "hello";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("消息发送完毕");
    }
}
