package com.qf.shop_goods_service;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(value = "com.qf.dao")
@SpringBootApplication(scanBasePackages = "com.qf")
@DubboComponentScan(value = "com.qf.service.impl")
public class ShopGoodsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopGoodsServiceApplication.class, args);
    }

}
