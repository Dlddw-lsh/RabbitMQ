package com.lsh.rabbitmq.exchange.deadQueue;

import com.lsh.rabbitmq.utils.RabbitUtils;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;

/**
 * @author lsh
 * @description: 消费者2
 */
public class Consumer02 {

    // 普通交换机的名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    // 死信交换机的名称
    public static final String DEAD_EXCHANGE = "dead_exchange";
    // 普通队列的名称
    public static final String NORMAL_QUEUE = "normal_queue";
    // 死信队列的名称
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();
        System.out.println("等待接受消息");
        channel.basicConsume(DEAD_QUEUE, true, (consumerTag, message) -> {
            System.out.println("Consumer02接受的消息" + new String(message.getBody(), StandardCharsets.UTF_8));
        }, consumerTag -> {
        });
    }
}
