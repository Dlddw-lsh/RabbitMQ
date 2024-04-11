package com.lsh.rabbitmqspring.listener;

import com.lsh.rabbitmqspring.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author lsh
 * @description:
 */
@Slf4j
@Component
public class Consumer {

    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void receiveConfirmMessage(Message message) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("接受到的队列confirm.queue消息：{}", msg);
    }

    @RabbitListener(queues = ConfirmConfig.WARING_QUEUE_NAME)
    public void waringConsumer(Message message) {
        String msg = new String(message.getBody());
        log.error("报警发现不可路由消息：{}", msg);
    }
}
