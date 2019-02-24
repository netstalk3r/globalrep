package spring.cloud.gateway.security.authorization;

import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.endpoint.ReactiveOAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import spring.cloud.gateway.security.OAuth2AccessTokenBodyExtractor;

@Component
public class WebClientReactiveRefreshTokenResponseClient implements ReactiveOAuth2AccessTokenResponseClient<OAuth2RefreshTokenGrantRequest> {

    private final WebClient webClient;

    public WebClientReactiveRefreshTokenResponseClient(WebClient webClient) {
        Assert.notNull(webClient, "webClient cannot be null");
        this.webClient = webClient;
    }

    @Override
    public Mono<OAuth2AccessTokenResponse> getTokenResponse(OAuth2RefreshTokenGrantRequest authorizationGrantRequest) {
        return Mono.defer(() -> {

            ClientRegistration clientRegistration = authorizationGrantRequest.getClientRegistration();

            // TODO timeout

            return webClient.post()
                    .uri(clientRegistration.getProviderDetails().getTokenUri())
                    .accept(MediaType.APPLICATION_JSON)
                    .headers(headers -> {
                        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                        headers.setBasicAuth(clientRegistration.getClientId(), clientRegistration.getClientSecret());
                    })
                    .body(refreshTokenBody(authorizationGrantRequest.getOAuth2AuthorizedClient().getRefreshToken().getTokenValue()))
                    .exchange()
                    .flatMap(OAuth2AccessTokenBodyExtractor.TOKEN_EXTRACTOR)
                    .map(response -> OAuth2AccessTokenBodyExtractor.SCOPE_EXTRACTOR.apply(response, clientRegistration));
        });
    }

    private BodyInserters.FormInserter<String> refreshTokenBody(String refreshToken) {
        return BodyInserters
                .fromFormData(OAuth2ParameterNames.GRANT_TYPE, AuthorizationGrantType.REFRESH_TOKEN.getValue())
                .with(OAuth2ParameterNames.REFRESH_TOKEN, refreshToken);
    }
}
