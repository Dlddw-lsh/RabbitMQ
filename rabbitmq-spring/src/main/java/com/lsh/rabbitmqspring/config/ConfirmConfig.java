package com.lsh.rabbitmqspring.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lsh
 * @description: 配置类 发布确认（高级）
 */
@Configuration
public class ConfirmConfig {
    public static final String CONFIRM_EXCHANGE_NAME = "confirm_exchange";
    public static final String CONFIRM_QUEUE_NAME = "confirm_queue";
    public static final String CONFIRM_ROUTING_KEY = "key1";

    // 备份交换机
    public static final String BACKUP_EXCHANGE_NAME = "backup_exchange";
    public static final String BACKUP_QUEUE_NAME = "backup_queue";
    // 报警
    public static final String WARING_QUEUE_NAME = "waring_queue";

    @Bean
    public DirectExchange confirmExchange() {
        return ExchangeBuilder
                .directExchange(CONFIRM_EXCHANGE_NAME)
                .durable(true)
                .alternate(BACKUP_EXCHANGE_NAME)
                .build();
    }

    @Bean
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    @Bean
    public Binding queueBindingExchange() {
        return BindingBuilder.bind(confirmQueue()).to(confirmExchange()).with(CONFIRM_ROUTING_KEY);
    }

    @Bean
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    @Bean
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }

    @Bean
    public Queue waringQueue() {
        return QueueBuilder.durable(WARING_QUEUE_NAME).build();
    }

    @Bean
    public Binding backQueueBingingBackupExchange() {
        return BindingBuilder.bind(backupQueue()).to(backupExchange());
    }

    @Bean
    public Binding waringQueueBingingBackupExchange() {
        return BindingBuilder.bind(waringQueue()).to(backupExchange());
    }
}
