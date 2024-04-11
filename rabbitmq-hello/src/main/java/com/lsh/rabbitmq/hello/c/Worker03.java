package com.lsh.rabbitmq.hello.c;

import com.lsh.rabbitmq.utils.RabbitUtils;
import com.lsh.rabbitmq.utils.SleepUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费在手动应答时不丢失，放回队列中重新消费
 */
public class Worker03 {

    public static final String TASK_QUEUE_NAME = "ask_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitUtils.getChannel();
        System.out.println("C1等待接受消息处理时间较短");

        // 设置不公平分发 为1的时候则会采用能者多劳的方式
        // int prefetchCount = 1;
        // 设置预取值
        int prefetchCount = 2;
        channel.basicQos(prefetchCount);

        // 采用手动应答
        channel.basicConsume(TASK_QUEUE_NAME, false, ((consumerTag, message) -> {

            System.out.println("C1接受到的消息:" + new String(message.getBody()));
            // 沉睡1s
            SleepUtils.sleep(1);
            // 手动应答
            /*
             * 1.消息的标记
             * 2.是否批量应答
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            System.out.println("C1消息处理完毕" + new String(message.getBody()));
        }), consumerTag -> {
            System.out.println("消息取消的回调");
        });
    }
}