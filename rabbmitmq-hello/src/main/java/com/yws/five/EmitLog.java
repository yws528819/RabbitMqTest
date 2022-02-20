package com.yws.five;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.yws.utils.MqUtils;

import java.util.Scanner;

/**
 * 发消息 交换机 （扇出类型）
 */
public class EmitLog {
    //交换机名称
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception{
        Channel channel = MqUtils.getChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
            System.out.println("生产者发出消息：" + message);
        }

    }
}
