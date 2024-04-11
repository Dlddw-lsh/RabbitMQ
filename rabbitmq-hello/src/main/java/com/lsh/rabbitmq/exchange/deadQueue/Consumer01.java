package com.lsh.rabbitmq.exchange.deadQueue;

import com.lsh.rabbitmq.utils.RabbitUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lsh
 * @description: 消费者1
 */
public class Consumer01 {

    // 普通交换机的名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    // 死信交换机的名称
    public static final String DEAD_EXCHANGE = "dead_exchange";
    // 普通队列的名称
    public static final String NORMAL_QUEUE = "normal_queue";
    // 死信队列的名称
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtils.getChannel();
        // 声明死信和普通的交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        // 声明普通队列
        Map<String, Object> arguments = new HashMap<>();
        // 过期时间
//        arguments.put("x-message-ttl", 100000);
        // 正常队列设置死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        // 正常队列设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key", "lisi");
        // 设置正常队列的长度的限制
//        arguments.put("x-max-length", 6);

        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);
        // 声明死信队列
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

        // 绑定普通的交换机与队列
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
        // 绑定死信的交换机与队列
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");
        System.out.println("等待接受消息");
        // 开启收到应答
        channel.basicConsume(NORMAL_QUEUE, false, (consumerTag, message) -> {
            String msg = new String(message.getBody());
            if (msg.equals("info5")) {
                System.out.println("Consumer01接收到的消息是：" + msg + " : 此消息是被C1拒绝的");
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
            } else {
                System.out.println("Consumer01接受的消息" + new String(message.getBody(), StandardCharsets.UTF_8));
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);

            }
        }, consumerTag -> {
        });
    }
}
