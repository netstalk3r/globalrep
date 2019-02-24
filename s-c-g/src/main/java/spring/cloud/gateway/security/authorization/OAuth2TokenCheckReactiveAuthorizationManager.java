package spring.cloud.gateway.security.authorization;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;

@Component
public class OAuth2TokenCheckReactiveAuthorizationManager<T> implements ReactiveAuthorizationManager<T>, InitializingBean {

    private final ClientRegistration clientRegistration;
    private final ServerOAuth2AuthorizedClientRepository authorizedClientRepository;
    private final WebClientReactiveRefreshTokenResponseClient reactiveRefreshTokenResponseClient;

    private Duration accessTokenExpiresSkew = Duration.ofMinutes(1);

    @Value("${spring.security.oauth2.client.registration.scg.min-token-expiration-seconds-to-refresh}")
    private int minExpirationSecondsToRefresh;

    public OAuth2TokenCheckReactiveAuthorizationManager(ClientRegistration clientRegistration,
                                                        ServerOAuth2AuthorizedClientRepository authorizedClientRepository,
                                                        WebClientReactiveRefreshTokenResponseClient reactiveRefreshTokenResponseClient) {
        Assert.notNull(authorizedClientRepository, "authorizedClientRepository cannot be null");
        Assert.notNull(clientRegistration, "clientRegistration cannot be empty");
        Assert.notNull(reactiveRefreshTokenResponseClient, "reactiveRefreshTokenResponseClient cannot be empty");
        this.clientRegistration = clientRegistration;
        this.authorizedClientRepository = authorizedClientRepository;
        this.reactiveRefreshTokenResponseClient = reactiveRefreshTokenResponseClient;
    }

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, T authorizationContext) {
        return authentication.flatMap(a -> checkAccessTokenExpiration((OAuth2AuthenticationToken) a, ((AuthorizationContext) authorizationContext).getExchange()))
                .defaultIfEmpty(new AuthorizationDecision(false));
    }


    private Mono<AuthorizationDecision> checkAccessTokenExpiration(OAuth2AuthenticationToken authentication, ServerWebExchange webExchange) {
        if (authentication.isAuthenticated()) {
            return authorizedClient(webExchange, authentication)
                    .flatMap(authorizedClient -> {
                        Instant expiresAt = authorizedClient.getAccessToken().getExpiresAt();
                        if (Instant.now().isAfter(expiresAt.minus(accessTokenExpiresSkew))) {
                            return reactiveRefreshTokenResponseClient.getTokenResponse(new OAuth2RefreshTokenGrantRequest(clientRegistration, authorizedClient))
                                    .flatMap(accessTokenResponse -> {
                                        OAuth2AuthorizedClient refreshedAuthorizedClient = new OAuth2AuthorizedClient(
                                                clientRegistration,
                                                authentication.getName(),
                                                accessTokenResponse.getAccessToken(),
                                                accessTokenResponse.getRefreshToken());
                                        return authorizedClientRepository.saveAuthorizedClient(refreshedAuthorizedClient, authentication, webExchange);
                                    })
                                    .thenReturn(new AuthorizationDecision(true));
                        }
                        return Mono.just(new AuthorizationDecision(true));
                    });
        }

        return Mono.just(new AuthorizationDecision(false));
    }

    private Mono<OAuth2AuthorizedClient> authorizedClient(ServerWebExchange exchange, OAuth2AuthenticationToken oauth2Authentication) {
        return this.authorizedClientRepository.loadAuthorizedClient(
                oauth2Authentication.getAuthorizedClientRegistrationId(), oauth2Authentication, exchange);
    }


    @Override
    public void afterPropertiesSet() {
        if (minExpirationSecondsToRefresh > 0) {
            this.accessTokenExpiresSkew = Duration.ofSeconds(minExpirationSecondsToRefresh);
        }
    }
}
