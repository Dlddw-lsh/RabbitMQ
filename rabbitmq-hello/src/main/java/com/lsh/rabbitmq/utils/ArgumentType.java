package com.lsh.rabbitmq.utils;

public enum ArgumentType {
    XMessageTTL("x-message-ttl"),
    XDeadLetterExchange("x-dead-letter-exchange"),
    XDeadLetterRoutingKey("x-dead-letter-routing-key");

    private final String type;

    ArgumentType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
