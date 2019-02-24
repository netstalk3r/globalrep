package spring.cloud.gateway.security.authorization;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.endpoint.AbstractOAuth2AuthorizationGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.Assert;

public class OAuth2RefreshTokenGrantRequest extends AbstractOAuth2AuthorizationGrantRequest {

    private final ClientRegistration clientRegistration;

    private final OAuth2AuthorizedClient oAuth2AuthorizedClient;

    public OAuth2RefreshTokenGrantRequest(ClientRegistration clientRegistration, OAuth2AuthorizedClient oAuth2AuthorizedClient) {
        super(AuthorizationGrantType.REFRESH_TOKEN);
        Assert.notNull(clientRegistration, "clientRegistration cannot be null");
        Assert.notNull(oAuth2AuthorizedClient, "oAuth2AuthorizedClient cannot be null");
        this.clientRegistration = clientRegistration;
        this. oAuth2AuthorizedClient = oAuth2AuthorizedClient;
    }

    public ClientRegistration getClientRegistration() {
        return clientRegistration;
    }

    public OAuth2AuthorizedClient getOAuth2AuthorizedClient() {
        return oAuth2AuthorizedClient;
    }
}
