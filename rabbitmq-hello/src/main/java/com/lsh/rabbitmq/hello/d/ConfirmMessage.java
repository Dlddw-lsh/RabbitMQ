package com.lsh.rabbitmq.hello.d;

import com.lsh.rabbitmq.utils.RabbitUtils;
import com.rabbitmq.client.Channel;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 发布确认模式
 * 使用的时间 比较哪种方式最好
 * 1.单个确认       -->     1575ms
 * 2.批量确认       -->     171ms
 * 3.异步批量确认    -->     100ms
 */
public class ConfirmMessage {
    private static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        // 1.单个确认
//        publicMessageIndividually();
        // 2.批量确认
//        publishMessageBatch();
        // 3.异步批量确认
        publicMessageAsync();
    }

    // 单个确认
    private static void publicMessageIndividually() throws Exception {
        Channel channel = RabbitUtils.getChannel();
        // 队列的声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        // 开启发布确认
        channel.confirmSelect();

        // 开始时间
        long begin = System.currentTimeMillis();

        // 批量发消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            // 单个消息就马上进行发布确认
            boolean success = channel.waitForConfirms();
            if (success) {
                System.out.println("消息发送成功");
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息，耗时" + (end - begin) + "ms");
    }

    // 批量发布确认
    public static void publishMessageBatch(String s1, String s2) throws Exception {
        Channel channel = RabbitUtils.getChannel();
        // 队列的声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        // 开启发布确认
        channel.confirmSelect();

        // 开始时间
        long begin = System.currentTimeMillis();

        // 批量确认消息的大小
        int batchSize = 100;
        // 批量发消息
        for (int i = 1; i <= MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());

            // 判断达到100条消息的时候 批量确认一次
            if (i % batchSize == 0) {
                // 发布确认
                channel.waitForConfirms();
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息，耗时" + (end - begin) + "ms");
    }

    // 异步确认发布
    public static void publicMessageAsync() throws Exception {
        Channel channel = RabbitUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);

        channel.confirmSelect();
        /*
         * 线程安全有序的一个哈希表，适用于高并发的情况下
         */
        ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

        // 准备消息的监听器，监听哪些消息成功了，那些消息失败了
        // 1.监听成功的 deliveryTag消息的编号 multiple是否为批量确认
        // 2.监听失败的
        channel.addConfirmListener((deliveryTag, multiple) -> {
            // ->2:删除已经确认的消息
            if (multiple) {
                // headMap是严格返回key小于deliveryTag的集合
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(deliveryTag);
                confirmed.clear();
            } else {
                outstandingConfirms.remove(deliveryTag);
            }
            System.out.println("确认的消息：" + deliveryTag);
        }, (deliveryTag, multiple) -> {
            //3:打印以下未确认的消息
            String message = outstandingConfirms.get(deliveryTag);
            System.out.println("未确认的消息：" + message);
            System.out.println("未确认的Tag：" + deliveryTag);
        }); // 异步通知
        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            //->1:记录所有要发送的消息
            outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
            channel.basicPublish("", queueName, null, message.getBytes());
        }
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个异步确认消息，耗时" + (end - begin) + "ms");
    }
}
