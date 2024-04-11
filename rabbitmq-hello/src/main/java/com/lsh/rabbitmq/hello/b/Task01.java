package com.lsh.rabbitmq.hello.b;

import com.lsh.rabbitmq.utils.RabbitUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 生产者 可以发送大量的消息
 */
public class Task01 {

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitUtils.getChannel();
        channel.queueDeclare(RabbitUtils.QUEUE_NAME, false, false, false, null);
        // 从控制台当中接受消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("", RabbitUtils.QUEUE_NAME, null, message.getBytes());
            System.out.println("消息发送完成:" + message);
        }
    }
}
