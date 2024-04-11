package com.lsh.rabbitmq.exchange.deadQueue;

import com.lsh.rabbitmq.utils.RabbitUtils;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;

/**
 * @author lsh
 * @description: 死信队列的生产者
 */
public class Producer {
    // 普通交换机的名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();

        // 死信消息 设置过期时间为单位是ms
//        AMQP.BasicProperties properties = new AMQP.BasicProperties()
//                .builder().expiration("10000").build();
        for (int i = 1; i <= 10; i++) {
            String message = "info" + i;
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan",
                    null, message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
