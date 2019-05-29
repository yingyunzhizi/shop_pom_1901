package com.qf.shop_search_service;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@DubboComponentScan("com.qf.service.impl")
@SpringBootApplication(scanBasePackages = "com.qf")
public class ShopSearchServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopSearchServiceApplication.class, args);
    }

}
