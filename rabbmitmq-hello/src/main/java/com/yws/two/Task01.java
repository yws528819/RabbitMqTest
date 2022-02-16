package com.yws.two;

import com.rabbitmq.client.Channel;
import com.yws.utils.MqUtils;

import java.util.Scanner;

/**
 * 消息生产者（使用控制台输入）
 */
public class Task01 {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception{
        Channel channel = MqUtils.getChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("发送消息完成：" + message);
        }
    }
}
