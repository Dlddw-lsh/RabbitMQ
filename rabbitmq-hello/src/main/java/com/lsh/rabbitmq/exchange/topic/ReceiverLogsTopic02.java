package com.lsh.rabbitmq.exchange.topic;

import com.lsh.rabbitmq.utils.RabbitUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author lsh
 * @description: 声明主题交换机及相关队列
 * C1
 */
public class ReceiverLogsTopic02 {

    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitUtils.getChannel();
        // 声明队列
        String queueName = "Q2";
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, EXCHANGE_NAME, "*.*.rabbit");
        channel.queueBind(queueName, EXCHANGE_NAME, "lazy.#");
        System.out.println("等待接受消息");

        // 接受消息
        channel.basicConsume(queueName, false, (consumerTag, message) -> {
            System.out.println(new String(message.getBody(), StandardCharsets.UTF_8));
            System.out.println("接收队列: " + queueName + " 绑定键：" + message.getEnvelope().getRoutingKey());
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        }, consumerTag -> {
            System.out.println("消息被取消了");
        });
    }
}
