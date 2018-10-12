package com.paem;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.setOrder(-199);  // go after zuul mapping
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/login").setViewName("login");
    }
}
