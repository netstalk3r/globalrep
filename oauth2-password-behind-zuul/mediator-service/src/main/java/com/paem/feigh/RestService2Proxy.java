package com.paem.feigh;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "web-service-2")
@RibbonClient(name = "web-service-2")
public interface RestService2Proxy {

    @RequestMapping("/insecure")
    String insecure();

    @RequestMapping("/secure")
    String secure();

}
