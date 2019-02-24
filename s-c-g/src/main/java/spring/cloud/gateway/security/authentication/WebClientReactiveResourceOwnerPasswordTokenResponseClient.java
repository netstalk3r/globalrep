package spring.cloud.gateway.security.authentication;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.endpoint.ReactiveOAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import spring.cloud.gateway.security.OAuth2AccessTokenBodyExtractor;

import java.util.Set;
import java.util.function.Consumer;


@Component
public class WebClientReactiveResourceOwnerPasswordTokenResponseClient implements ReactiveOAuth2AccessTokenResponseClient<OAuth2ResourceOwnerPasswordGrantRequest> {

    private final WebClient webClient;

    public WebClientReactiveResourceOwnerPasswordTokenResponseClient(WebClient webClient) {
        Assert.notNull(webClient, "webClient cannot be null");
        this.webClient = webClient;
    }

    @Override
    public Mono<OAuth2AccessTokenResponse> getTokenResponse(OAuth2ResourceOwnerPasswordGrantRequest authorizationGrantRequest) {
        return Mono.defer(() -> {
            ClientRegistration clientRegistration = authorizationGrantRequest.getClientRegistration();

            String tokenUri = clientRegistration.getProviderDetails().getTokenUri();
            BodyInserters.FormInserter<String> body = body(authorizationGrantRequest);

            // TODO timeout

            return this.webClient.post()
                    .uri(tokenUri)
                    .accept(MediaType.APPLICATION_JSON)
                    .headers(headers(clientRegistration))
                    .body(body)
                    .exchange()
                    .flatMap(OAuth2AccessTokenBodyExtractor.TOKEN_EXTRACTOR)
                    .map(response -> OAuth2AccessTokenBodyExtractor.SCOPE_EXTRACTOR.apply(response, clientRegistration));
        });
    }

    private Consumer<HttpHeaders> headers(ClientRegistration clientRegistration) {
        return headers -> {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBasicAuth(clientRegistration.getClientId(), clientRegistration.getClientSecret());
        };
    }

    private static BodyInserters.FormInserter<String> body(OAuth2ResourceOwnerPasswordGrantRequest authorizationGrantRequest) {
        ClientRegistration clientRegistration = authorizationGrantRequest.getClientRegistration();
        BodyInserters.FormInserter<String> body = BodyInserters
                .fromFormData(OAuth2ParameterNames.GRANT_TYPE, authorizationGrantRequest.getGrantType().getValue());
        Set<String> scopes = clientRegistration.getScopes();
        if (!CollectionUtils.isEmpty(scopes)) {
            String scope = StringUtils.collectionToDelimitedString(scopes, " ");
            body.with(OAuth2ParameterNames.SCOPE, scope);
        }
        UsernamePasswordAuthenticationToken userCredentials = authorizationGrantRequest.getUserCredentials();
        body.with("username", userCredentials.getPrincipal().toString());
        body.with("password", userCredentials.getCredentials().toString());
        return body;
    }
}
