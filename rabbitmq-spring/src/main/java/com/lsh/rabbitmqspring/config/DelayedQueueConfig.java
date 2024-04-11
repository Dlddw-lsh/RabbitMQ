package com.lsh.rabbitmqspring.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lsh
 * @description:
 */
@Configuration
public class DelayedQueueConfig {

    // 交换机
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    // 队列
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    // routingKey
    public static final String DELAYED_ROUTING_KEY = "delayed.routingKey";

    @Bean
    public Queue delayedQueue() {
        return new Queue(DELAYED_QUEUE_NAME);
    }

    // 声明交换机 基于插件
    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type", "direct");
        /*
            1.交换机的名称
            2.交换机的类型
            3.是否需要持久化
         */
        return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message", true, false, arguments);
    }

    @Bean
    public Binding delayedQueueBindingDelayedExchange() {
        return BindingBuilder
                .bind(delayedQueue())
                .to(delayedExchange())
                .with(DELAYED_ROUTING_KEY).noargs();
    }

}
