package com.qf.shop_goods_service;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version 1.0
 * @data 5/28/2019 20:25
 * @user yingyunzhizi
 */
@Configuration
public class RabbitMqConfit {

    @Bean
    public Queue getQueue1(){
        return new Queue("search_queue");
    }

    @Bean
    public Queue getQueue2(){
        return new Queue("item_queue");
    }

    @Bean
    public FanoutExchange getExchange(){
        return (FanoutExchange)ExchangeBuilder.fanoutExchange("goods_exchange").build();
    }

    /**
     * 将队列和交换机绑定
     * @param getQueue1
     * @param getExchange
     * @return
     */
    @Bean
    public Binding bind1(Queue getQueue1,FanoutExchange getExchange){
        return BindingBuilder.bind(getQueue1).to(getExchange);
    }

    @Bean
    public Binding bind2(Queue getQueue2,FanoutExchange getExchange){
        return BindingBuilder.bind(getQueue2).to(getExchange);
    }
}
