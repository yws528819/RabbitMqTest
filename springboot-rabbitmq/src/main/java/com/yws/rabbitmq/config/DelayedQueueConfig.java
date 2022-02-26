package com.yws.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 基于插件的延迟队列（延迟交换机发送队列的时间）
 */
@Configuration
public class DelayedQueueConfig {

    //队列
    public static final String DELAYED_QUEUE = "delayed.queue";
    //交换机
    public static final String DELAYED_EXCHANGE = "delayed.exchange";
    //RoutingKey
    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";

    //声明交换机 基于插件的
    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type", "direct");
        //参数依次为交换机名称、类型、是否需要持久化、是否自动删除、其他参数
        return new CustomExchange(DELAYED_EXCHANGE, "x-delayed-message", true, false, arguments);
    }

    //声明队列
    @Bean
    public Queue delayedQueue() {
        return QueueBuilder.durable(DELAYED_QUEUE).build();
    }

    //绑定
    @Bean
    public Binding delayedQueueBingdingDelayedExchange() {
        return BindingBuilder.bind(delayedQueue()).to(delayedExchange()).with(DELAYED_ROUTING_KEY).noargs();
    }

}
