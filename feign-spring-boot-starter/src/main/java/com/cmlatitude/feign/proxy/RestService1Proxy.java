package com.cmlatitude.feign.proxy;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "service1")
@RibbonClient(name = "service1")
public interface RestService1Proxy {

    @RequestMapping("/insecure")
    String insecure();

    @RequestMapping("/secure")
    String secure();

}
