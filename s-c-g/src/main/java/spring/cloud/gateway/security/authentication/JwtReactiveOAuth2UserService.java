package spring.cloud.gateway.security.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtReactiveOAuth2UserService implements ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private static final String USER_NAME_ATTRIBUTE_NAME = "user_name";
    private static final String AUTHORITIES_ATTRIBUTE_NAME = "authorities";

    @Autowired
    private ReactiveJwtDecoder jwtDecoder;

    @Override
    public Mono<OAuth2User> loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        return  jwtDecoder.decode(userRequest.getAccessToken()
                .getTokenValue())
                .map(jwt -> new DefaultOAuth2User(toGrantedAuthorities(jwt.getClaims()) ,jwt.getClaims(), USER_NAME_ATTRIBUTE_NAME));
    }

    private Collection<? extends GrantedAuthority> toGrantedAuthorities(Map<String,Object> claims){
        Collection<String> stringAuthorities = (Collection<String>) claims.get(AUTHORITIES_ATTRIBUTE_NAME);
        return CollectionUtils.isEmpty(stringAuthorities) ? null
                : stringAuthorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

}
