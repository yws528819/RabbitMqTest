package com.yws.three;

import com.rabbitmq.client.Channel;
import com.yws.utils.MqUtils;

import java.util.Scanner;

/**
 * 消息手动应答时是不丢失，放回队列中重新消费
 */
public class Task2 {

    //队列名称
    private static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = MqUtils.getChannel();

        channel.queueDeclare(TASK_QUEUE_NAME, false, false, false, null);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("", TASK_QUEUE_NAME, null, message.getBytes());
            System.out.println("生产者发出消息：" + message);
        }

    }
}
