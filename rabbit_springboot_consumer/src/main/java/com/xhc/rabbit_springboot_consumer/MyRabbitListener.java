package com.xhc.rabbit_springboot_consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @data 5/28/2019 20:06
 * @user yingyunzhizi
 */
@Component
public class MyRabbitListener {

    @RabbitListener(queues = "hehe_queue")
    public void myHandler(String msg){
        System.out.println("消息是:"+msg);
    }
}
