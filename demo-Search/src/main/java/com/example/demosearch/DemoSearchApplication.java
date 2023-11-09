package com.example.demosearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient // 开启服务注册发现功能
public class DemoSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoSearchApplication.class, args);
    }

}
