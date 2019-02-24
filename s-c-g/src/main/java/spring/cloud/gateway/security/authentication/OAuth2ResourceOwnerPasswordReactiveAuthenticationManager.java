package spring.cloud.gateway.security.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.endpoint.ReactiveOAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;

@Component
public class OAuth2ResourceOwnerPasswordReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
    private ReactiveOAuth2AccessTokenResponseClient<OAuth2ResourceOwnerPasswordGrantRequest> accessTokenResponseClient;

    @Autowired
    private ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> userService;

    @Autowired
    private ClientRegistration clientRegistration;

    private GrantedAuthoritiesMapper authoritiesMapper = (authorities -> authorities);

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.defer(() -> Mono.just(clientRegistration)
                .map(clientRegistration -> new OAuth2ResourceOwnerPasswordGrantRequest(clientRegistration, (UsernamePasswordAuthenticationToken) authentication))
                .flatMap(this.accessTokenResponseClient::getTokenResponse)
                .flatMap(accessTokenResponse -> authenticationResult(clientRegistration, accessTokenResponse)));
    }

    private Mono<OAuth2AuthenticationToken> authenticationResult(ClientRegistration clientRegistration, OAuth2AccessTokenResponse accessTokenResponse) {
        OAuth2AccessToken accessToken = accessTokenResponse.getAccessToken();
        Map<String, Object> additionalParameters = accessTokenResponse.getAdditionalParameters();
        return userService.loadUser(new OAuth2UserRequest(clientRegistration, accessToken, additionalParameters))
                .map(oAuth2User -> {
                    Collection<? extends GrantedAuthority> mappedAuthorities =
                            authoritiesMapper.mapAuthorities(oAuth2User.getAuthorities());

                    OAuth2AuthenticationToken oAuth2AuthenticationToken = new OAuth2AuthenticationToken(oAuth2User,
                            mappedAuthorities,
                            clientRegistration.getRegistrationId());
                    oAuth2AuthenticationToken.setDetails(accessTokenResponse);
                    return oAuth2AuthenticationToken;
                });
    }
}
