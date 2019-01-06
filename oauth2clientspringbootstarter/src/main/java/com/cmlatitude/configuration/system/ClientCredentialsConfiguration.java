package com.cmlatitude.configuration.system;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

@Configuration
public class ClientCredentialsConfiguration {

    @Bean
    @ConfigurationProperties("security.oauth2.client")
    public OAuth2ProtectedResourceDetails resourceDetails() {
        return new ClientCredentialsResourceDetails();
    }

    @Bean
    public AccessTokenProvider accessTokenProvider() {
        return new ClientCredentialsAccessTokenProvider();
    }
}
