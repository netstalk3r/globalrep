package spring.cloud.gateway.security.logout;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import reactor.core.publisher.Mono;

public class RemoveAuthorizedClientServerLogoutSuccessHandler implements ServerLogoutSuccessHandler {

    private final ClientRegistration clientRegistration;

    private final ServerOAuth2AuthorizedClientRepository authorizedClientRepository;

    public RemoveAuthorizedClientServerLogoutSuccessHandler(ClientRegistration clientRegistration, ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {
        this.clientRegistration = clientRegistration;
        this.authorizedClientRepository = authorizedClientRepository;
    }

    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {
        return authorizedClientRepository.removeAuthorizedClient(
                clientRegistration.getRegistrationId(), authentication, exchange.getExchange());
    }
}
