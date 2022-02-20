package com.yws.six;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.yws.utils.MqUtils;

import java.util.Scanner;

/**
 * 发消息 交换机 （direct类型）
 */
public class DirectLogs {
    //交换机名称
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception{
        Channel channel = MqUtils.getChannel();

        //消费者声明了也可以不写
        //channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            //这里指定消息健，看要发给谁就改成对应的，比如info, warning, error
            channel.basicPublish(EXCHANGE_NAME, "info", null, message.getBytes());
            System.out.println("生产者发出消息：" + message);
        }

    }
}
