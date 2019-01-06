package com.cmlatitude.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.OAuth2AccessTokenSupport;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.util.Collections;

@Configuration
@ConditionalOnClass({EnableFeignClients.class, EnableResourceServer.class})
public class OAuth2SystemClientAutoConfiguration {

    @Autowired
    private LoadBalancerInterceptor loadBalancerInterceptor;

    @Autowired
    private AccessTokenProvider tokenProvider;

    @Autowired
    @Qualifier("resourceDetails")
    private OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails;

    @Bean
    public OAuth2FeignRequestInterceptor oAuth2FeignRequestInterceptor() {
        OAuth2FeignRequestInterceptor oAuth2FeignRequestInterceptor = new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), oAuth2ProtectedResourceDetails);

        ((OAuth2AccessTokenSupport)tokenProvider).setInterceptors(Collections.singletonList(loadBalancerInterceptor));

        oAuth2FeignRequestInterceptor.setAccessTokenProvider(new AccessTokenProviderChain(Collections.singletonList(tokenProvider)));

        return oAuth2FeignRequestInterceptor;
    }

}
