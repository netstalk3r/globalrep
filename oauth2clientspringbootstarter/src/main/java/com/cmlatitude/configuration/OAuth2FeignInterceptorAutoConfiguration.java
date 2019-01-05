package com.cmlatitude.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.context.WebApplicationContext;

@Configuration
@ConditionalOnClass({EnableFeignClients.class, EnableResourceServer.class})
public class OAuth2FeignInterceptorAutoConfiguration {

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.INTERFACES)
    @ConditionalOnMissingBean
    public OAuth2ClientContext oAuth2ClientContext() {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        DefaultOAuth2AccessToken oAuth2AccessToken = new DefaultOAuth2AccessToken(details.getTokenValue());
        oAuth2AccessToken.setTokenType(details.getTokenType());
        return new DefaultOAuth2ClientContext(oAuth2AccessToken);
    }

    @Bean
    @Autowired
    public OAuth2FeignRequestInterceptor oAuth2FeignRequestInterceptor(OAuth2ClientContext oAuth2ClientContext) {
        return new OAuth2FeignRequestInterceptor(oAuth2ClientContext, null) {
            @Override
            public OAuth2AccessToken getToken() {
                return oAuth2ClientContext.getAccessToken();
            }
        };
    }

}
