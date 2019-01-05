package com.paem.config;

import com.cmlatitude.annotation.EnableOAuth2TokenInterceptor;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

@Configuration
@EnableOAuth2TokenInterceptor
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
    @Scope(value = "process", proxyMode = ScopedProxyMode.INTERFACES)
    public OAuth2ClientContext oAuth2ClientContext() {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        DefaultOAuth2AccessToken oAuth2AccessToken = new DefaultOAuth2AccessToken(details.getTokenValue());
        oAuth2AccessToken.setTokenType(details.getTokenType());
        return new DefaultOAuth2ClientContext(oAuth2AccessToken);
    }

}
