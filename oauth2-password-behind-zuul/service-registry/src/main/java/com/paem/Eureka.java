package com.paem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class Eureka {
    // TODO registry refresh interval
    public static void main(String[] args) {
        SpringApplication.run(Eureka.class, args);
    }
}
