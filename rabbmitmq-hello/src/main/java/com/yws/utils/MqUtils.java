package com.yws.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class MqUtils {
    /**
     * 获取mq信道
     * @return
     * @throws Exception
     */
    public static Channel getChannel() throws Exception{
        //连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //IP、用户名、密码
        factory.setHost("192.168.0.105");
        factory.setUsername("admin");
        factory.setPassword("123");
        //创建信道、连接
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        return channel;
    }

}
