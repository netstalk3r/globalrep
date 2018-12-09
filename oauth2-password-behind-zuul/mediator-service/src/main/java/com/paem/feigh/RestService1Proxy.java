package com.paem.feigh;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "web-service-1")
@RibbonClient(name = "web-service-1")
public interface RestService1Proxy {

    @RequestMapping("/insecure")
    String insecure();

    @RequestMapping("/secure")
    String secure();

}
