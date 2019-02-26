package com.cmlatitude.feign.proxy;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "service2")
@RibbonClient(name = "service2")
public interface RestService2Proxy {

    @RequestMapping("/insecure")
    String insecure();

    @RequestMapping("/secure")
    String secure();

}
