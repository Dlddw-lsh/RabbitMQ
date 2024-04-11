package com.lsh.rabbitmq.hello.a;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费者 接受消息
 */
public class Consumer {

    // 队列的名称
    public static final String QUEUE_NAME = "hello";

    // 接受消息
    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 工厂IP 连接RabbitMQ
        factory.setHost("localhost");
        // 用户名和密码
        factory.setUsername("admin");
        factory.setPassword("123456");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        /*
         * 消费者消费消息
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答
         * 3.消费者成功消费的回调函数
         * 4.消费者取消消费的回调
         */
        channel.basicConsume(QUEUE_NAME, true,
                (consumerTag, message) -> System.out.println(consumerTag + "---->" + new String(message.getBody())),
                (String consumerTage) -> System.out.println("消费者消费被中断"));
    }
}
