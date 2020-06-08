package com.xuecheng.test.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author: 韩老魔
 * @Date: 2019/11/21 0021 17:14
 */
@SpringBootApplication
@EnableScheduling
public class ProductRabbitmqApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductRabbitmqApplication.class, args);
    }
}
