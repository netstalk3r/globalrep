package com.cmlatitude;


import feign.Feign;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients
@ConditionalOnClass(Feign.class)
public class FeignAutoConfiguration {
}
