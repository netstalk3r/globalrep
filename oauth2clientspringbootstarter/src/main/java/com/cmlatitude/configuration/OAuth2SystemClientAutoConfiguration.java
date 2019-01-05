package com.cmlatitude.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.util.Collections;

@Configuration
@ConditionalOnClass({EnableFeignClients.class, EnableResourceServer.class})
public class OAuth2SystemClientAutoConfiguration {

//    @Bean
//    @ConfigurationProperties("security.oauth2.client")
//    public ResourceOwnerPasswordResourceDetails clientDetails() {
//        return new ResourceOwnerPasswordResourceDetails();
//    }

    @Bean
    @ConfigurationProperties("security.oauth2.client")
    public ClientCredentialsResourceDetails clientDetails() {
        return new ClientCredentialsResourceDetails();
    }

    @Bean
    @Autowired
    public OAuth2FeignRequestInterceptor oAuth2FeignRequestInterceptor(LoadBalancerInterceptor loadBalancerInterceptor) {
        OAuth2FeignRequestInterceptor oAuth2FeignRequestInterceptor = new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), clientDetails());

//        ResourceOwnerPasswordAccessTokenProvider tokenProvider = new ResourceOwnerPasswordAccessTokenProvider();
        ClientCredentialsAccessTokenProvider tokenProvider = new ClientCredentialsAccessTokenProvider();
        tokenProvider.setInterceptors(Collections.singletonList(loadBalancerInterceptor));

        oAuth2FeignRequestInterceptor.setAccessTokenProvider(new AccessTokenProviderChain(Collections.singletonList(tokenProvider)));

        return oAuth2FeignRequestInterceptor;
    }

}
