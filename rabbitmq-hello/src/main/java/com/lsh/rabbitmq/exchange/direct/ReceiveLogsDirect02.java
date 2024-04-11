package com.lsh.rabbitmq.exchange.direct;

import com.lsh.rabbitmq.utils.RabbitUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author lsh
 * @description:
 * @date 2023-09-24 10:56:21
 */
public class ReceiveLogsDirect02 {
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitUtils.getChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        channel.queueDeclare("disk", false, false, false, null);

        channel.queueBind("disk", EXCHANGE_NAME, "error");

        channel.basicConsume("disk", (consumerTag, message) -> {
            System.out.println("ReceiveLogs02控制台接收到的消息:" + new String(message.getBody(), StandardCharsets.UTF_8));
        }, ((consumerTag, sig) -> {
            System.out.println(consumerTag + "：消息被取消");
        }));
    }
}
