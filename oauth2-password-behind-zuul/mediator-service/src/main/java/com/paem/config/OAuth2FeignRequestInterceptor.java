package com.paem.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * Interceptor to include OAuth2AccessToken to any request to downstream services
 */

public class OAuth2FeignRequestInterceptor implements RequestInterceptor {

    /**
     * The logger instance used by this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2FeignRequestInterceptor.class);

    /**
     * The authorization header name.
     */
    private static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * Current OAuth2 authentication context.
     */
    private final OAuth2ProcessTokenContext oauth2TokenContext;

    /**
     * Creates new instance of {@link OAuth2FeignRequestInterceptor} with access token.
     *
     * @param oauth2TokenContext the OAuth2 access token
     */
    public OAuth2FeignRequestInterceptor(OAuth2ProcessTokenContext oauth2TokenContext) {
        Assert.notNull(oauth2TokenContext, "Context can not be null");
        this.oauth2TokenContext = oauth2TokenContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply(RequestTemplate template) {
        if (template.headers().containsKey(AUTHORIZATION_HEADER)) {
            throw new IllegalArgumentException("There should not be any Authorization header");
        } else if (oauth2TokenContext.getOAuth2AccessToken() == null || oauth2TokenContext.getoAuth2TokeType() == null) {
            throw new IllegalArgumentException("OAuth2AccessToken and OAuth2AccessTokeType should not be null");
        } else {
            LOGGER.debug("Constructing Header {} for Token {}", AUTHORIZATION_HEADER, oauth2TokenContext.getoAuth2TokeType());
            template.header(AUTHORIZATION_HEADER, String.format("%s %s", oauth2TokenContext.getoAuth2TokeType(),
                    oauth2TokenContext.getOAuth2AccessToken()));
        }
    }
}