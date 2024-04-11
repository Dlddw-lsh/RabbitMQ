package com.lsh.rabbitmq.hello.c;

import com.lsh.rabbitmq.utils.RabbitUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 消息在手动应答时是不丢失，放回队列中重新消费
 */
public class Task2 {
    public static final String TASK_QUEUE_NAME = "ask_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitUtils.getChannel();
        // 声明队列
        // 需要让Queue进行持久化
        boolean durable = true;
        channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);

        // 从控制台中输入信息
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String message = scanner.next();
            // 设置生产者发送消息为持久化消息（要求保存到磁盘上） MessageProperties.PERSISTENT_TEXT_PLAIN
            channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println("生产者发出消息:" + message);
        }
    }
}
