package com.lsh.rabbitmq.hello.b;

import com.lsh.rabbitmq.utils.RabbitUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 工作线程 相当于之前的消费者
 */
public class Worker01 {

    public static void main(String[] args) throws IOException, TimeoutException {
        new Thread(() -> {
            Channel channel = null;
            try {
                channel = RabbitUtils.getChannel();
                channel.basicConsume(RabbitUtils.QUEUE_NAME, true,
                        (consumerTag, message) -> System.out.println("C1接收到的消息：" + new String(message.getBody())),
                        consumerTag -> System.out.println(consumerTag + "消息取消"));
            } catch (IOException | TimeoutException e) {
                throw new RuntimeException(e);
            }

            System.out.println("C1等待接受消息....");
        }).start();

        new Thread(() -> {
            Channel channel = null;
            try {
                channel = RabbitUtils.getChannel();
                channel.basicConsume(RabbitUtils.QUEUE_NAME, true,
                        (consumerTag, message) -> System.out.println("C2接收到的消息：" + new String(message.getBody())),
                        consumerTag -> System.out.println(consumerTag + "消息取消"));
            } catch (IOException | TimeoutException e) {
                throw new RuntimeException(e);
            }

            System.out.println("C2等待接受消息....");


        }).start();
    }
}
