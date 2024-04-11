package com.lsh.rabbitmqspring.controller;

import com.lsh.rabbitmqspring.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lsh
 * @description: 开始发消息 发布确认
 */
@Slf4j
@RestController("/confirm")
public class ProducerController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMessage/{msg}")
    public void sendMessage(@PathVariable String msg) {
        CorrelationData correlationData = new CorrelationData("1");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY, msg, correlationData);

        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY + "1", msg, correlationData);
        log.info("发送消息内容为：{}", msg);
    }
}
