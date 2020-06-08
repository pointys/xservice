package com.xuecheng.govern.center;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author 韩浩辰
 * @date 2020/5/27 16:09
 */
@SpringBootApplication
@EnableEurekaServer
public class GoverCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoverCenterApplication.class, args);
    }
}
