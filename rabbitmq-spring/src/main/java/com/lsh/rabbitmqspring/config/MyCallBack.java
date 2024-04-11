package com.lsh.rabbitmqspring.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Slf4j
@Component
public class MyCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    @Resource
    private RabbitTemplate rabbitTemplate;

    // 注入
    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 交换机确认回调方法
     * 1. 发消息 交换机接收到了 回调
     *
     * @param correlationData 保存回调信息的ID及相关信息
     * @param isAck           交换机是否收到信息
     * @param errorMsg        失败的原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean isAck, String errorMsg) {
        if (isAck) {
            log.info("交换机已经收到id为：{}的消息", correlationData.getId());
        } else {
            log.info("交换机还未收到id为：{}的消息，原因是{}", correlationData.getId(), errorMsg);
        }

    }

    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        Message message = returnedMessage.getMessage();
        String exchange = returnedMessage.getExchange();
        String replyText = returnedMessage.getReplyText();
        String routingKey = returnedMessage.getRoutingKey();
        log.error("消息：{}， 被交换机{}回退，退回原因：{}，路由Key:{}",
                new String(message.getBody()), exchange, replyText, routingKey);
    }
}
