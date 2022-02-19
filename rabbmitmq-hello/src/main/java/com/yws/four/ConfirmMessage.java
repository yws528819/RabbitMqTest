package com.yws.four;

import com.rabbitmq.client.Channel;
import com.yws.utils.MqUtils;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 发布确认模式：用于生产者确认自己成功发送消息否
 *  1、单个确认
 *  2、批量确认
 *  3、异步批量确认
 */
public class ConfirmMessage {
    private static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception{
        //单个确认
        //publishMessageIndividually();

        //批量确认
        //publishMessageBatch();

        //异步确认
        publishMessageAsync();
    }


    /**
     * 单个确认
     */
    public static void publishMessageIndividually() throws Exception{
        Channel channel = MqUtils.getChannel();

        //声明队列
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);

        //开启发布确认
        channel.confirmSelect();

        //开始时间
        long begin = System.currentTimeMillis();

        //批量发消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message =  i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            //单个消息马上进行发布确认
            boolean waitForConfirms = channel.waitForConfirms();
            if (waitForConfirms) {
                System.out.println("消息发送成功");
            }
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个独立确认消息，耗时" + (end - begin) + "ms");
    }


    /**
     * 批量发布确认
     */
    public static void publishMessageBatch() throws Exception{
        Channel channel = MqUtils.getChannel();

        //声明队列
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);

        //开启发布确认
        channel.confirmSelect();

        //开始时间
        long begin = System.currentTimeMillis();

        //批量确认消息条数大小
        int batchSize = 100;

        for (int i = 1; i <= MESSAGE_COUNT ; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            if (i % batchSize == 0) {
                channel.waitForConfirms();
            }
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息，耗时" + (end - begin) + "ms");
    }


    /**
     * 异步发布确认消息
     * @throws Exception
     */
    public static void publishMessageAsync() throws Exception {
        Channel channel = MqUtils.getChannel();

        //声明队列
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);

        //开启发布确认
        channel.confirmSelect();

        /**
         * 线程安全有序的一个哈希表 适用于高并发的情况下
         * 1.轻松的将序号与消息进行关联
         * 2.轻松批量删除条目 只要给到序号
         * 3.支持高并发（多线程）
         */
        ConcurrentSkipListMap<Long, String> concurrentSkipListMap = new ConcurrentSkipListMap<>();

        //准备消息的监听器 监听哪些消息成功了 哪些消息失败了
        channel.addConfirmListener(
                (deliveryTag, multiple) -> {
                    System.out.println("确认的消息：" + deliveryTag);
                    //2：删除已经确认的消息 剩下的就是未确认的消息
                    if (multiple) {
                        //批量确认场景
                        ConcurrentNavigableMap<Long, String> confirmed = concurrentSkipListMap.headMap(deliveryTag, true);
                        confirmed.clear();
                    }else {
                        //单个确认
                        concurrentSkipListMap.remove(deliveryTag);
                    }

                },
                (deliveryTag, multiple) -> {
                    //3：打印一下未确认的消息有哪些
                    String message = concurrentSkipListMap.get(deliveryTag);
                    System.out.println("未确认的消息是：" + message + "::::未确认的消息tag：" + deliveryTag);
                });

        //开始时间
        long begin = System.currentTimeMillis();

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            //1：此处记录下所有要发送的消息 消息的总和
            concurrentSkipListMap.put(channel.getNextPublishSeqNo(), message);

            channel.basicPublish("", queueName, null, message.getBytes());
        }

        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个异步确认消息，耗时" + (end - begin) + "ms");
    }
}
