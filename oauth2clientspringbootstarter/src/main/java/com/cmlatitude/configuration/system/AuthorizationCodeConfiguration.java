package com.cmlatitude.configuration.system;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

@Configuration
public class AuthorizationCodeConfiguration {

    @Bean
    @ConfigurationProperties("security.oauth2.client")
    public OAuth2ProtectedResourceDetails resourceDetails() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    public AccessTokenProvider accessTokenProvider() {
        return new AuthorizationCodeAccessTokenProvider();
    }
}
