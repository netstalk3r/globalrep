package com.paem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
public class Gateway {
    // TODO investigate how to debug downstream services
    public static void main(String[] args) {
        SpringApplication.run(Gateway.class, args);
    }
}
