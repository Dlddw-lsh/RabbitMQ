package com.lsh.rabbitmq.utils;

/**
 * 睡眠工具类
 */
public class SleepUtils {
    public static void sleep(int second){
        try {
            Thread.sleep(1000L * second);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
