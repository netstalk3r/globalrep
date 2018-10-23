# Spring-Boot OAuth2: `AuthorizationServer` behind `Zuul` load balancer Gateway (Resource Owner Password Credentials Grant Type)

## Introduction
This article describes how to create Spring Boot application with OAuth2 Resource Owner Password Credentials Grant Type.

To learn what is OAuth2 protocol see the specification - https://tools.ietf.org/html/rfc6749

From [OAuth2 Specification](https://tools.ietf.org/html/rfc6749#section-4.3):
*The resource owner password credentials grant type is suitable in cases where the resource owner has a trust relationship with the client*

This example is a draft, the code quality is not perfect and is used just to demonstrate the configuration, not to use in production.

## Useful links
During working on this example there are a couple of articles (besides Spring articles [#1](https://projects.spring.io/spring-security-oauth/docs/oauth2.html), [#2](https://spring.io/guides/tutorials/spring-boot-oauth2/), [#3](https://docs.spring.io/spring-security-oauth2-boot/docs/current/reference/htmlsingle/)) that helped me a lot:
 * [Spring Boot OAuth2 workflow behind Zuul for internal client authorization](http://lifeinide.com/post/2018-04-14-spring-oauth2-zuul-internal-external-client-workflow/)
 * [UAA (AuthorizationServer) load balanced behind API-GATEWAY (Edge service Zuul)](https://github.com/kakawait/uaa-behind-zuul-sample)
 
## Overview
The goal is to build an application which consists of next micro-services:
* API-GATEWAY - `Zuul`
* SERVICE REGISTRY - `Eureka`
* AUTHORIZATION SERVICE - Spring Boot OAuth2 Authorization Server, for issuing access tokens
* RESOURCE SERVICE - Spring Boot OAuth2 Resource servers, token is required to access them

Access Token is JwtToken.

## Usage

In each service folder run following command:

```sh
mvn spring-boot:run
```

Open http://localhost:8765/login and login with credentials user/user or admin/admin

## Service communication

1) Login flow - obtaining access token

    ```
    Browser                             Zuul                               Auth
       │         /login (GET)            │                                  │
       ├────────────────────────────────>│                                  │
       │  Location:http://ZUUL/login     │                                  │
       │<┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┤                                  │
       │        /login  (POST)           │                                  │
       ├────────────────────────────────>│                                  │
       │                                 │        /oauth/token (POST)       │
       │                                 │   HTTP Basic Authentification    │
       │                                 ├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄>│
       │                                 │                                  │
       │                                 │          {Access Token}          │
       │                                 │<┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┤
       │                                 │                                  │
       │    Location:http://ZUUL/index   │                                  │
       │<┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┤                                  │
       │                                 │                                  │
    
    ```

2) Service request flow - make request to resource service via gateway
    ```
    Browser                             Zuul                            Resource
       │  /api/service1/secure (GET)     │                                  │
       ├────────────────────────────────>│                                  │
       │                                 │                                  │
       │                                 │           /secure (GET)          │
       │                                 │     HTTP Basic Authorization:    │
       │                                 │       bearer {access token}      │
       │                                 ├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄>│
       │                                 │                                  │
       │                                 │         Resource response        │
       │                                 │<┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┤
       │                                 │                                  │
       │        Resource response        │                                  │
       │<┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┤                                  │
       │                                 │                                  │
    
    ```
## Services configuration

### Service registry

```java
@SpringBootApplication
@EnableEurekaServer
public class Eureka {
    public static void main(String[] args) {
        SpringApplication.run(Eureka.class, args);
    }
}
```
* `@EnableEurekaServer` - in conjunction with configuration, this stands up the registry

```yaml
spring:
  application:
    name: service-registry

server:
  port: 8761

eureka:
  instance:
    hostname: localhost
  client:
    registerWithEureka: false
    fetchRegistry: false
```

* `spring.application.name` - service name
* `server.port` - port that is user to run the registry
* `eureka.instance.hostname` - eureka hostname, this hostname should be specified to other services so they can register in service registry
* `eureka.client.registerWithEureka` - specifies whether to register itself in registry or not
* `eureka.client.fetchRegistry` - specifies whether to pull registry or not

### Authorization Service

Authorization Service consists of two parts:
* `AuthorizationServer` - configuration for the OAuth2 Service that issues tokens
* `SecurityConfiguration` - configuration of the http security protection and login configuration

#### `SecurityConfigurations`

```java
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
```

The class extends `WebSecurityConfigurerAdapter` to configure security.

```java
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                .withUser("admin")
                .password("admin")
                .roles("ADMIN")
                .and()
                .withUser("user")
                .password("user")
                .roles("USER");
    }
```

Users, that can login in the application, are configured using `AuthenticationManagerBuilder`. They are stored in memory. The password is not encrypted.
<br/>**Note**: `NoOpPasswordEncoder` is used, because by default spring expects passwords to be already encrypted.
`NoOpPasswordEncoder` is deprecated. [Alternately password can be prefixed with *{noop}* prefix](https://spring.io/blog/2017/11/01/spring-security-5-0-0-rc1-released#password-storage-updated). 

```java
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().fullyAuthenticated()
                .and().httpBasic();
    }
```

Security is told that every request should be authenticated with HTTP Basic authentication. <br> 
**Note**: in this case Basic authentication required **client_id** and **client_secret**, not user and password.

```java
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    @Bean
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }
}
```

`AuthenticationManager` and `UserDetailsService` are exposed to use in OAuth2 `AuthorizationServer` configuration.
`AuthenticationManager` is required to authenticate the users. It uses `UserDetailsService` internally to perform authentication. However to support **refresh_token** grant type, 
`UserDetailsService` should be exposed also, because **refresh_token** flow does not authenticate users, but directly loads their info from `UserDetailsService`.

#### `AuthorizationServer`

```java
@Configuration
@EnableAuthorizationServer
@SpringBootApplication
@EnableEurekaClient
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter
```
`AuthorizationServer` class extends `AuthorizationServerConfigurerAdapter` to configure OAuth2 Authorization Server. `@EnableAuthorizationServer` works together with `AuthorizationServerConfigurerAdapter`.
`@EnableEurekaClient` by using this annotation, service makes registration on Service Registry when service starts up.

```java
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("keypair.jks"), "test-pass".toCharArray());
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("test"));
        return converter;
    }
```
To use `RsaSigner` for JWToken, private and public keys are required. On the `AuthorizationServer` these keys are stored in the Java Key Store on the Server - **_keypair.jks_**.
When Access and Refresh tokens are issued, they encoded and signed using private keys, then Resource Servers can check token signature and decode it by using public key.
Every Resource server should have public key. 

```java
    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    @Qualifier("userDetailsServiceBean")
    private UserDetailsService userDetailsService;
```
Injecting `AuthenticationManager` and `UserDetailsService` to use them for user authentication.

```java
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.tokenKeyAccess("isAuthenticated()")
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }
```
Specify the access to the `AuthorizationServer` endpoints. It is said that for '/oauth/token' endpoint all requests should be authenticated.
Additionally specify `NoOpPasswordEncoder` so spring expects passwords as plain text, not as encrypted string.

```java
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("example_id")
                .secret("example_secret")
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("read", "write")
                .autoApprove(true) // This client won’t display approval screen to get access to selected oauth2 scopes
                .accessTokenValiditySeconds(60*60)
                .refreshTokenValiditySeconds(120 * 60);
    }
```

Configuration of the clients, who can issue tokens on this `AuthorizationServer`. One in-memory client is specified with **client_id** and **client_secret**,
**password**, **refresh_token** grant types and **scopes**. Auto approve to true means server will auto approve all scopes.
In the end Access Token and Refresh Token validity is specified.

```java
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .reuseRefreshTokens(false) // is to use non-reusable refresh token
                .accessTokenConverter(accessTokenConverter());
    }
```

Configuration of the Token Endpoints itself. `AuthenticationManager` and `UserDetailsService` are specified to authenticate users that were configured in `SecurityConfigurations`.
<br>
By default **refresh_token** has a very long lifetime. To use non-reusable refresh token `reuseRefreshTokens(false)` configuration is set.
<br> 
Configured token converter is passed, that has private and public keys, to `AuthorizationServer`, so it knows how to encode and sign tokens. 
<br> **Note**: see `SecurityConfigurations` for details why `AuthorizationServer` requires `AuthenticationManager` and `UserDetailsService`.

```yaml
spring:
  application:
    name: auth-service


server:
  port: 9099
  servlet:
    context-path: /


eureka:
  client:
    serviceUrl:
      defaultZone: http://${eureka.instance.hostname}:${eureka.instance.port}/eureka/
  instance:
    hostname: localhost
    port: 8761
```
Almost all of these properties were already described in the previous service. Here only new properties are explained.
* `server.servlet.contex-path` - service context path, it is the same by default.
* `eureka.client.serviceUtl.defaultZone` - Eureka endpoint

#### Resource Service

```java
@SpringBootApplication
@EnableEurekaClient
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class RestService1 {
```

* `@EnableResourceServer` - does everything related to OAuth2 security, creates token store, adds `OAuth2AuthenticationProcessingFilter`, etc.
* `@EnableGlobalMethodSecurity(prePostEnabled = true)` - enable processing of `PreAuthorize` and `PostAuthorize` annotations.

```java
    @PreAuthorize("#oauth2.hasScope('read') and hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/secure")
    public String secure(Principal p) {
        return String.format("Secure Hello %s from Web Service 1", p.getName());
    }
```
Security for the api /secure

```yaml
spring:  
  application:
    name: web-service-1

server:
  port: 9092

eureka:
  instance:
    hostname: localhost
    port: 8761
  client:
    serviceUrl:
      defaultZone: http://${eureka.instance.hostname}:${eureka.instance.port}/eureka/

security:
  oauth2:
    resource:
      jwt:
        key-value: |
          -----BEGIN PUBLIC KEY-----
          MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhr48B5ICsfOIfd4S/+x1j+pKpLcxKsiiF1J7jbZeneTRMKL+peZCnrvLCqKAODyir5AyYKqv8T1PnczfOceif+zKJ0cTr4Lmz7I0P1lEhakpjJIxAGtWktPtKWX3ZK7uX8galzOYhcWeOPYL+MJIUSkwRA2pdIsjC8wf/6qIaz19C8URNG1ciBirXre5Ambd6guQ5hKKnzWMZdz77lAKbpVK2RRIY6+5r0BR/NxMcn5Lgp7ZzBFA+nwCaqRnKFPbjceKBQzNY7wKut/TvdQCIW6btdU6/WT8IOY5HIOqCAyq/BQmU2jOM0WGhvcl2NOgLI0vdZ1oC0Uu34HG+YgQLwIDAQAB
          -----END PUBLIC KEY-----
```
The key part here is `security.oauth2.resource.jwt.key-value` - this is the public key, which is used to verify key signature and decode it.
`@EnableResourceServer` - creates converter with RSA Verifier, which is used in token store.

### Api-Gateway Service
This service is the key communication part of the system. For browser it manages simple sessions, but for the internal services it manages OAuth2 tokens.
It has mapping between user sessions and its access tokens.
<br> Additionally in this example this service serves static content. That may not be good for production, but it is ok for the example.

#### Zuul Configuration

```java
@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
public class Gateway {
    public static void main(String[] args) {
        SpringApplication.run(Gateway.class, args);
    }
}
```
* `@EnableZuulProxy` - together with configurations boots zuul proxy server.

```yaml
zuul:
  prefix: /api
  routes:
    service1:
      path: /service1/**
      serviceId: web-service-1
    service2:
      path: /service2/**
      serviceId: web-service-2
```
* `zuul.prefix` - uri prefix for zuul server
* `zuul.routes` - routes configuration for proxy
* `zuul.routes.service1` - config for particular proxy, _service1_ - is the name of the route, it can be any string
* `zuul.routes.service1.path` - uri for requests that will be proxied to _service1_
* `zuul.routes.service1.serviceId` - service name from the service-registry to proxy requests to

#### MVC Configuration

```java
@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.setOrder(-199);  // go after zuul mapping
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/login").setViewName("login");
    }
}
``` 

```yaml
spring:
  mvc:
    view:
      suffix: .html
```

Registering static content. 
<br> Next is the main configuration part of the Api-Gateway. It is consist of security configuration and OAuth2 client configuration.
First OAuth2 client configuration is explained and then how it fits into Spring Security.

#### OAuth2 Client Configuration

```yaml
security:
  oauth2:
    sso:
      loginPath: /login
    client:
      accessTokenUri: http://auth-service/oauth/token
      clientId: example_id
      clientSecret: example_secret
    resource:
      jwt:
        key-value: |
          -----BEGIN PUBLIC KEY-----
          MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhr48B5ICsfOIfd4S/+x1j+pKpLcxKsiiF1J7jbZeneTRMKL+peZCnrvLCqKAODyir5AyYKqv8T1PnczfOceif+zKJ0cTr4Lmz7I0P1lEhakpjJIxAGtWktPtKWX3ZK7uX8galzOYhcWeOPYL+MJIUSkwRA2pdIsjC8wf/6qIaz19C8URNG1ciBirXre5Ambd6guQ5hKKnzWMZdz77lAKbpVK2RRIY6+5r0BR/NxMcn5Lgp7ZzBFA+nwCaqRnKFPbjceKBQzNY7wKut/TvdQCIW6btdU6/WT8IOY5HIOqCAyq/BQmU2jOM0WGhvcl2NOgLI0vdZ1oC0Uu34HG+YgQLwIDAQAB
          -----END PUBLIC KEY-----
```

* `security.oauth2.sso.loginPath` - login path for OAuth2 SSO
* `security.oauth2.client` - OAuth2 client configuration
* `security.oauth2.client.accessTokenUri` - url for obtaining access token, it is the uri of `AuthorizationServer`, note it is defined with service name from registry
* `security.oauth2.client.clientId` - *client_id* that is specified on`AuthorizationServer`
* `security.oauth2.client.clientSecret` - *client_secret* that is specified on`AuthorizationServer`
* `security.oauth2.resource` - the same configuration as for Resource Service

```java
@Configuration
@EnableOAuth2Client
@Order(value = 0)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
```
Security aspect of the Api-Gateway is configured the same way as Authorization Server, by extending `WebSecurityConfigurerAdapter`.
There is one class `SecurityConfiguration` for security and OAuth2 client configuration in the example.
<br> * `@EnableOAuth2Client` - marker for configuring `OAuth2ClientContext` where appropriate access token for the request is stored

```java
    @Bean
    @ConfigurationProperties(prefix = "security.oauth2.sso")
    public OAuth2SsoProperties sso() {
        return new OAuth2SsoProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "security.oauth2.resource")
    public ResourceServerProperties resourceDetails() {
        return new ResourceServerProperties();
    }

    @Bean
    @ConfigurationProperties("security.oauth2.client")
    public ResourceOwnerPasswordResourceDetails clientDetails() {
        return new ResourceOwnerPasswordResourceDetails();
    }
```

These beans are holding values from yaml configurations.

```java
    @Bean
    public JwtAccessTokenConverter tokenEnhancer() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setVerifierKey(resourceDetails().getJwt().getKeyValue());
        return converter;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(tokenEnhancer());
    }

    @Bean
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setReuseRefreshToken(false);
        return tokenServices;
    }
```
Configuration of `TokenService`. 
* `JwtAccessTokenConverter` - token converter which aware of public key and know how to verify and decode token.
* `TokenStore` - in our case it is `JwtTokenStore`, that actually does not store tokens, but know how to decode it, using given token converter.
* `DefaultTokenServices` - token service with uses token store, and additional configurations about refresh token, that reflects Authorization server configuration.
 

```java
    @Autowired
    private OAuth2ClientContext oauth2ClientContext;

    @Autowired
    private LoadBalancerInterceptor loadBalancerInterceptor;
```
* `OAuth2ClientContext` - to pass it to `OAuth2RestTemplate`, which will be created later, so it knows where to get Access Token from.
* `LoadBalancerInterceptor` - is needed for `RestTemplate` instances, so they know how to resolve service names in uri.
 

```java
    @Bean
    public OAuth2RestOperations oAuth2RestTemplateBean() {
        ResourceOwnerPasswordAccessTokenProvider tokenProvider = new ResourceOwnerPasswordAccessTokenProvider();
        tokenProvider.setInterceptors(Collections.singletonList(loadBalancerInterceptor));

        OAuth2RestTemplate oauth2Template = new OAuth2RestTemplate(clientDetails(), oauth2ClientContext);
        oauth2Template.setInterceptors(Collections.singletonList(loadBalancerInterceptor));
        oauth2Template.setAccessTokenProvider(new AccessTokenProviderChain(Collections.singletonList(tokenProvider)));

        return oauth2Template;
    }
```
* `OAuth2RestOperations` - configuration of `OAuth2RestTemplate`. 
  * Create token provider, which in our case is `ResourceOwnerPasswordAccessTokenProvider` and pass it `loadBalancerInterceptor`, so it can resolve service names. 
  * Create `OAuth2RestTemplate` with `OAuth2ClientContext`, so it knows where to get access tokens from and
client details, that are specified in the yaml configurations. Configure it with `loadBalancerInterceptor` and `ResourceOwnerPasswordAccessTokenProvider` created above.

 **Note**: 
`loadBalancerInterceptor` is passed to `OAuth2RestTemplate` and `ResourceOwnerPasswordAccessTokenProvider`.
It looks strange, because `OAuth2RestTemplate` uses `ResourceOwnerPasswordAccessTokenProvider` internally. But in practice,
these are two independent classes, and they all need to be configured with `loadBalancerInterceptor`.

```java
    private Filter ssoFilter() {
        OAuth2ClientAuthenticationProcessingFilter oAuth2ClientAuthenticationProcessingFilter = new OAuth2ClientAuthenticationProcessingFilter(sso().getLoginPath());
        oAuth2ClientAuthenticationProcessingFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(sso().getLoginPath(), HttpMethod.POST.name()));
        oAuth2ClientAuthenticationProcessingFilter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler("/index"));

        oAuth2ClientAuthenticationProcessingFilter.setRestTemplate(oAuth2RestTemplateBean());

        oAuth2ClientAuthenticationProcessingFilter.setTokenServices(tokenServices());
        return oAuth2ClientAuthenticationProcessingFilter;
    }
```

The key part of the client configuration. All preceding configuration was done to be able to configure `OAuth2ClientAuthenticationProcessingFilter`.
This is the filter where OAuth2 authentication happens - where Api-Gateway is getting the access token.
<br> First filter is created and configured with:
 * login uri - the uri that filter will process to perform login
 * authenticationSuccessHandler - what to do if authentication is successful
 * `OAuth2RestTemplate` - so the filter can obtain access token
 * `TokenService` - to load authentication by access token 

#### Security Configuration

```java
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers(sso().getLoginPath()).permitAll().anyRequest().authenticated()
                .and()
                .httpBasic().disable()
                .csrf().requireCsrfProtectionMatcher(csrfRequestMatcher()).csrfTokenRepository(csrfTokenRepository())
                .and()
                .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(csrfHeaderFilter(), CsrfFilter.class)
                .logout()
                .permitAll()
                .logoutSuccessUrl(sso().getLoginPath());
                addAuthenticationEntryPoint(http);

    }
```

* `.authorizeRequests().antMatchers(sso().getLoginPath()).permitAll().anyRequest().authenticated()` - specify login path, allow all requests to it, and all other requests should be authenticated.
* `.httpBasic().disable()` - disable HTTP Basic security
* `.csrf().requireCsrfProtectionMatcher(csrfRequestMatcher()).csrfTokenRepository(csrfTokenRepository())` - specify CSRF request matchers and token repository.
* `.addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class)` - add `OAuth2ClientAuthenticationProcessingFilter` filter into the filters chain.
* `.addFilterAfter(csrfHeaderFilter(), CsrfFilter.class)` - add CSRF filter to put token into cookie and header
* `logout().permitAll().logoutSuccessUrl(sso().getLoginPath());` - configure logout. 
* `addAuthenticationEntryPoint(http);` - configure entry point, to redirect in case of any issue


**Note**: this application uses CSRF token, but its configuration is out of scope of this example. 

```java
    private void addAuthenticationEntryPoint(HttpSecurity http)
            throws Exception {
        ExceptionHandlingConfigurer<HttpSecurity> exceptions = http.exceptionHandling();
        ContentNegotiationStrategy contentNegotiationStrategy = http
                .getSharedObject(ContentNegotiationStrategy.class);
        if (contentNegotiationStrategy == null) {
            contentNegotiationStrategy = new HeaderContentNegotiationStrategy();
        }
        MediaTypeRequestMatcher preferredMatcher = new MediaTypeRequestMatcher(
                contentNegotiationStrategy, MediaType.APPLICATION_XHTML_XML,
                new MediaType("image", "*"), MediaType.TEXT_HTML, MediaType.TEXT_PLAIN);
        preferredMatcher.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
        exceptions.defaultAuthenticationEntryPointFor(
                new LoginUrlAuthenticationEntryPoint(sso().getLoginPath()),
                preferredMatcher);
        // When multiple entry points are provided the default is the first one
        exceptions.defaultAuthenticationEntryPointFor(
                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest"));
    }
```
Entry points configuration, it is takes from Spring source code. Placed here to get rid of `@EnableResourceServer` annotation.


 