package spring.cloud.gateway.security.authentication;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

public class OAuth2LoginAuthenticationWebFilter extends AuthenticationWebFilter {

    private ClientRegistration clientRegistration;

    private ServerOAuth2AuthorizedClientRepository authorizedClientRepository;

    public OAuth2LoginAuthenticationWebFilter(ReactiveAuthenticationManager authenticationManager,
                                              ServerOAuth2AuthorizedClientRepository authorizedClientRepository,
                                              ClientRegistration clientRegistration) {
        super(authenticationManager);
        Assert.notNull(authorizedClientRepository, "authorizedClientRepository cannot be null");
        Assert.notNull(clientRegistration, "authorizedClientRepository cannot be null");
        this.authorizedClientRepository = authorizedClientRepository;
        this.clientRegistration = clientRegistration;
    }

    @Override
    protected Mono<Void> onAuthenticationSuccess(Authentication authentication, WebFilterExchange webFilterExchange) {
        OAuth2AuthenticationToken authenticationResult = (OAuth2AuthenticationToken) authentication;
        OAuth2AccessTokenResponse accessTokenResponse = (OAuth2AccessTokenResponse) authenticationResult.getDetails();
        OAuth2AuthorizedClient authorizedClient = new OAuth2AuthorizedClient(
                clientRegistration,
                authenticationResult.getName(),
                accessTokenResponse.getAccessToken(),
                accessTokenResponse.getRefreshToken());
        OAuth2AuthenticationToken result =  new OAuth2AuthenticationToken(
                authenticationResult.getPrincipal(),
                authenticationResult.getAuthorities(),
                clientRegistration.getRegistrationId());
        return authorizedClientRepository.saveAuthorizedClient(authorizedClient, authenticationResult, webFilterExchange.getExchange())
                .then(super.onAuthenticationSuccess(result, webFilterExchange));
    }
}
