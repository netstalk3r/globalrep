package com.cmlatitude.annotation;

import com.cmlatitude.configuration.OAuth2FeignInterceptorAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(OAuth2FeignInterceptorAutoConfiguration.class)
public @interface EnableOAuth2TokenInterceptor {
}
