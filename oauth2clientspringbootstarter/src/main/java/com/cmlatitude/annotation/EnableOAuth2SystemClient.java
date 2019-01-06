package com.cmlatitude.annotation;

import com.cmlatitude.configuration.OAuth2SystemClientAutoConfiguration;
import com.cmlatitude.configuration.system.OAuth2TokenProviderImportSelector;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.token.OAuth2AccessTokenSupport;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({OAuth2TokenProviderImportSelector.class, OAuth2SystemClientAutoConfiguration.class})
public @interface EnableOAuth2SystemClient {

    Class<? extends OAuth2AccessTokenSupport> provider() default ClientCredentialsAccessTokenProvider.class;

}
