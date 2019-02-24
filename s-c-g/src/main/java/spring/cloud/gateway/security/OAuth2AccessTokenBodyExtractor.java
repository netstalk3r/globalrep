package spring.cloud.gateway.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.web.reactive.function.OAuth2BodyExtractors;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class OAuth2AccessTokenBodyExtractor {

    public static final Function<ClientResponse, Mono<OAuth2AccessTokenResponse>> TOKEN_EXTRACTOR =
            clientResponse -> {
                HttpStatus httpStatus = clientResponse.statusCode();
                if (httpStatus.is4xxClientError()) {
                    throw new BadCredentialsException(httpStatus.getReasonPhrase());
                }

                if (httpStatus.is5xxServerError()) {
                    throw new AuthenticationServiceException(httpStatus.getReasonPhrase());
                }

                return clientResponse.body(OAuth2BodyExtractors.oauth2AccessTokenResponse());
            };

    public static final BiFunction<OAuth2AccessTokenResponse, ClientRegistration, OAuth2AccessTokenResponse> SCOPE_EXTRACTOR =
            (clientResponse, clientRegistration) -> {
                if (clientResponse.getAccessToken().getScopes().isEmpty()) {
                    clientResponse = OAuth2AccessTokenResponse.withResponse(clientResponse)
                            .scopes(clientRegistration.getScopes()).build();
                }
                return clientResponse;
            };

}
