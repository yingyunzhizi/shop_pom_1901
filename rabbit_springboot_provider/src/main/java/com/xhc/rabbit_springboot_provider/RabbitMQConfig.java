package com.xhc.rabbit_springboot_provider;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version 1.0
 * @data 5/28/2019 16:10
 * @user yingyunzhizi
 */
@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue getQueue(){
        return new Queue("hehe_queue");
    }
}
