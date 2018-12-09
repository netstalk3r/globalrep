package com.paem.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

@Configuration
public class OAuth2ProcessContextConfiguration {

    /**
     * Registering process custom scope
     * @return
     */
    @Bean
    public static BeanFactoryPostProcessor beanFactoryPostProcessor() {
        return beanFactory -> beanFactory.registerScope("process", new ProcessScope());
    }

    /**
     * Using process scope for OAuth2AccessToken
     * @return
     */
    @Bean
    @Scope(value = "process", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public OAuth2ProcessTokenContext processOauth2ClientContext() {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return new OAuth2ProcessTokenContext(details.getTokenValue(), details.getTokenType());
    }

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor(OAuth2ProcessTokenContext oAuth2ProcessTokenContext) {
        return new OAuth2FeignRequestInterceptor(oAuth2ProcessTokenContext);
    }

}
