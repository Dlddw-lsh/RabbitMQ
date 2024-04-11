package com.lsh.rabbitmqspring.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author lsh
 * @description: 消费者 基于插件的延迟消息
 */
@Slf4j
@Component
public class DelayQueueConsumer {

    @RabbitListener(queues = "delayed.queue")
    public void receiverDelayQueue(Message message) {
        String msg = new String(message.getBody());
        log.info("当前时间：{}，收到延迟队列的消息：{}", new Date(), msg);
    }
}
