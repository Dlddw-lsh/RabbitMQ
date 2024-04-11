package com.lsh.rabbitmq.exchange.fanout;

import com.lsh.rabbitmq.utils.RabbitUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 *
 */
public class ReceiveLogs02 {

    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitUtils.getChannel();
        // 声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        // 声明一个队列
        /*
         *  生产一个临时队列，队列的名称是随机的
         *  当消费者断开与队列的连接的时候，队列就自动删除
         */
        String queueName = channel.queueDeclare().getQueue();
        /*
         * 绑定交换机与队列
         */
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("等待接受消息......");
        channel.basicConsume(queueName, true, ((consumerTag, message) -> {
            System.out.println("02接受到的消息:" + new String(message.getBody(), StandardCharsets.UTF_8));
        }), consumerTag -> {
            System.out.println(consumerTag + "：消息被取消");
        });

    }
}
