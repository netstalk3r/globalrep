package spring.cloud.gateway.security.logout;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DelegatingServerLogoutSuccessHandler implements ServerLogoutSuccessHandler {

    private final List<ServerLogoutSuccessHandler> delegates = new ArrayList<>();

    public DelegatingServerLogoutSuccessHandler(Collection<ServerLogoutSuccessHandler> delegates) {
        Assert.notEmpty(delegates, "delegates cannot be null or empty");
        this.delegates.addAll(delegates);
    }

    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {
        return Mono.when(delegates.stream()
                .filter(Objects::nonNull)
                .map(delegate -> delegate.onLogoutSuccess(exchange, authentication))
                .collect(Collectors.toList()));
    }
}
