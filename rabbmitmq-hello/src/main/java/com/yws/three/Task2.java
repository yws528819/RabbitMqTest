package com.yws.three;

import com.rabbitmq.client.Channel;
import com.yws.utils.MqUtils;

/**
 * 消息手动应答时是不丢失，放回队列中重新消费
 */
public class Task2 {

    //队列名称
    private static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = MqUtils.getChannel();


    }
}
