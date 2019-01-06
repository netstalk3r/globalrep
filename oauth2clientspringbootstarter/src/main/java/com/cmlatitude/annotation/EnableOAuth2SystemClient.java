package com.cmlatitude.annotation;

import com.cmlatitude.configuration.OAuth2SystemClientAutoConfiguration;
import com.cmlatitude.configuration.system.OAuth2TokenProviderImportSelector;
import com.cmlatitude.configuration.system.SystemOAuth2Provider;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({OAuth2TokenProviderImportSelector.class, OAuth2SystemClientAutoConfiguration.class})
public @interface EnableOAuth2SystemClient {

    SystemOAuth2Provider provider() default SystemOAuth2Provider.CLIENT_CREDENTIALS;

}
