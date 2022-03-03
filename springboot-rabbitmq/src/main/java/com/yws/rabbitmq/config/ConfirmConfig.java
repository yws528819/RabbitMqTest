package com.yws.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类 发布确认
 */
@Configuration
public class ConfirmConfig {
    //交换机
    public static final String CONFIRM_EXCHANGE_NAME = "confirm_exchange";
    //队列
    public static final String CONFIRM_QUEUE_NAME = "confirm_queue";
    //RoutingKey
    public static final String CONFIRM_ROUTING_KEY = "key1";

    //===============备份交换机demo==================
    //备份交换机
    public static final String BACKUP_EXCHANGE_NAME = "backup_exchange";
    //备份队列
    public static final String BACKUP_QUEUE_NAME = "backup_queue";
    //报警队列
    public static final String WARNING_QUEUE_NAME = "warning_queue";



    //声明交换机
    @Bean
    public DirectExchange confirmExchange() {
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME)
                //绑定备份交换机
                .durable(true).alternate(BACKUP_EXCHANGE_NAME).build();
        //或者
        //.withArgument("alternate-exchange", BACKUP_EXCHANGE_NAME)
    }

    //声明队列
    @Bean
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    //绑定
    @Bean
    public Binding queueBindingExchange() {
        return BindingBuilder.bind(confirmQueue()).to(confirmExchange()).with(CONFIRM_ROUTING_KEY);
    }


    //声明备份交换机
    @Bean
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }
    //声明备份队列
    @Bean
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }
    //声明报警队列
    @Bean
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }
    //绑定
    @Bean
    public Binding backupQueueBindingBackupExchange() {
        return BindingBuilder.bind(backupQueue()).to(backupExchange());
    }
    //绑定
    @Bean
    public Binding warningQueueBindingBackupExchange() {
        return BindingBuilder.bind(warningQueue()).to(backupExchange());
    }



}
