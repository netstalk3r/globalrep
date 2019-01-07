package com.paem;

import com.cmlatitude.annotation.EnableOAuth2SystemClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableEurekaClient
@EnableOAuth2SystemClient
@EnableResourceServer
@EnableScheduling
public class Scheduler {
    public static void main(String[] args) {
        SpringApplication.run(Scheduler.class, args);
    }
}
