package com.lsh.rabbitmqspring.controller;

import com.lsh.rabbitmqspring.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author lsh
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMsgController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMsg/{msg}")
    public void sendMsg(@PathVariable String msg) {
        log.info("当前时间:{},发送一条信息给两个ttl队列:{}", new Date(), msg);
        rabbitTemplate.convertAndSend("X", "XA", "消息来自ttl为10s的队列：" + msg);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自ttl为40s的队列：" + msg);
    }

    // 开始发消息 消息 TTL
    @GetMapping("/sendExpirationMsg/{msg}/{ttl}")
    public void sendMsg(@PathVariable String msg, @PathVariable String ttl) {
        log.info("当前时间:{},发送一条时长{}毫秒的信息给ttl队列:{}", new Date(), ttl, msg);
        rabbitTemplate.convertAndSend("X", "XC", msg, message -> {
            // 发送消息的延迟时长
            message.getMessageProperties().setExpiration(ttl);
            return message;
        });
    }

    // 开始发消息 基于插件的 消息及延迟的时间
    @GetMapping("/sendDelayMsg/{msg}/{delayTime}")
    public void sendMsg(@PathVariable Integer delayTime, @PathVariable String msg) {
        log.info("当前时间:{},发送一条时长{}毫秒的信息给延迟队列:{}", new Date(), delayTime, msg);
        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME,
                DelayedQueueConfig.DELAYED_ROUTING_KEY, msg, message -> {
                    message.getMessageProperties().setDelay(delayTime);
                    return message;
                });
    }
}
