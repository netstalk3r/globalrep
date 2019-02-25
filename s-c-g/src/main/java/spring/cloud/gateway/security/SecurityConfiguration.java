package spring.cloud.gateway.security;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.ServerFormLoginAuthenticationConverter;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.security.web.server.csrf.CsrfWebFilter;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import spring.cloud.gateway.security.authentication.OAuth2LoginAuthenticationWebFilter;
import spring.cloud.gateway.security.logout.DelegatingServerLogoutSuccessHandler;
import spring.cloud.gateway.security.logout.RemoveAuthorizedClientServerLogoutSuccessHandler;
import sun.security.rsa.RSAPublicKeyImpl;

import java.io.StringReader;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(OAuth2ClientProperties.class)
public class SecurityConfiguration {

    @Value("${spring.security.oauth2.loginPath}")
    private String loginPath;

    @Value("${spring.security.oauth2.client.provider.uaa.jwt-key-value}")
    private String jwtPublicKeyValue;

    @Autowired
    private ReactiveAuthenticationManager manager;

    @Autowired
    private ReactiveAuthorizationManager tokenAuthorizationManager;

    @Autowired
    private ServerOAuth2AuthorizedClientRepository authorizedClientRepository;

    private OAuth2ClientProperties properties;

    public SecurityConfiguration(OAuth2ClientProperties properties) {
        this.properties = properties;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange()
                .pathMatchers(loginPath).permitAll()
                .anyExchange().access(tokenAuthorizationManager)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                // TODO CookieServerCsrfTokenRepository is used for AngularJS
//                .csrf().requireCsrfProtectionMatcher(requireCsrfProtectionMatcher()).csrfTokenRepository(new CookieServerCsrfTokenRepository())
                .csrf().requireCsrfProtectionMatcher(requireCsrfProtectionMatcher())
                .and()
                .exceptionHandling().authenticationEntryPoint(new RedirectServerAuthenticationEntryPoint(loginPath))
                .and()
                .logout().logoutSuccessHandler(createServerLogoutSuccessHandler())
                .and()
                .addFilterAt(authenticationWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public AuthenticationWebFilter authenticationWebFilter() {
        AuthenticationWebFilter authenticationFilter = new OAuth2LoginAuthenticationWebFilter(manager, authorizedClientRepository, clientRegistration()) ;
        authenticationFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, loginPath));
        authenticationFilter.setServerAuthenticationConverter(new ServerFormLoginAuthenticationConverter());
        authenticationFilter.setAuthenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler());
        authenticationFilter.setSecurityContextRepository(new WebSessionServerSecurityContextRepository());
        authenticationFilter.setAuthenticationFailureHandler((webFilterExchange, exception) -> Mono.error(exception));
//        authenticationFilter.setAuthenticationFailureHandler(new RedirectServerAuthenticationFailureHandler("/login?error"));
        return authenticationFilter;
    }

    private ServerLogoutSuccessHandler createServerLogoutSuccessHandler() {
        RedirectServerLogoutSuccessHandler successHandler = new RedirectServerLogoutSuccessHandler();
        successHandler.setLogoutSuccessUrl(URI.create(loginPath));
        return new DelegatingServerLogoutSuccessHandler(Arrays.asList(
                new RemoveAuthorizedClientServerLogoutSuccessHandler(clientRegistration(), authorizedClientRepository),
                successHandler));
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() throws Exception {
        PemReader pemReader = new PemReader(new StringReader(jwtPublicKeyValue));
        PemObject pem = pemReader.readPemObject();
        return new NimbusReactiveJwtDecoder(new RSAPublicKeyImpl(pem.getContent()));
    }

    @Bean
    public ClientRegistration clientRegistration() {
        Map.Entry<String, OAuth2ClientProperties.Registration> registrationEntry = properties.getRegistration().entrySet().iterator().next();
        OAuth2ClientProperties.Registration registration = registrationEntry.getValue();
        Map.Entry<String, OAuth2ClientProperties.Provider> providerEntry = properties.getProvider().entrySet().iterator().next();
        return ClientRegistration.withRegistrationId(registrationEntry.getKey())
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS) // WORKAROUND, no way to build ClientRegistrations other then predefined
                .clientId(registration.getClientId())
                .clientSecret(registration.getClientSecret())
                .scope(registration.getScope())
                .tokenUri(providerEntry.getValue().getTokenUri()).build();
    }

    @Bean
    public ReactiveClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryReactiveClientRegistrationRepository(Collections.singletonList(clientRegistration()));
    }

    @Bean
    public WebClient uaaWebClient(LoadBalancerClient loadBalancerClient) {
        return WebClient.builder()
                .filter(new LoadBalancerExchangeFilterFunction(loadBalancerClient))
                .build();
    }

    private ServerWebExchangeMatcher requireCsrfProtectionMatcher() {
        return new ServerWebExchangeMatcher() {

            private ServerWebExchangeMatcher loginPathMatcher = new NegatedServerWebExchangeMatcher(ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, loginPath));

            @Override
            public Mono<MatchResult> matches(ServerWebExchange exchange) {
                return CsrfWebFilter.DEFAULT_CSRF_MATCHER.matches(exchange)
                        .filter(MatchResult::isMatch)
                        .flatMap(matchResult -> loginPathMatcher.matches(exchange))
                        .switchIfEmpty(MatchResult.notMatch());
            }
        };
    }

}
