package spring.cloud.gateway.security.authentication;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.endpoint.AbstractOAuth2AuthorizationGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.Assert;

public class OAuth2ResourceOwnerPasswordGrantRequest extends AbstractOAuth2AuthorizationGrantRequest {

    private final ClientRegistration clientRegistration;

    private final UsernamePasswordAuthenticationToken userCredentials;

    public OAuth2ResourceOwnerPasswordGrantRequest(ClientRegistration clientRegistration, UsernamePasswordAuthenticationToken userCredentials) {
        super(new AuthorizationGrantType("password"));
        Assert.notNull(clientRegistration, "clientRegistration cannot be null");
        Assert.notNull(userCredentials, "token cannot be null");
        this.clientRegistration = clientRegistration;
        this.userCredentials = userCredentials;
    }

    public ClientRegistration getClientRegistration() {
        return clientRegistration;
    }

    public UsernamePasswordAuthenticationToken getUserCredentials() {
        return userCredentials;
    }
}
