package com.Life_flow.api_gateway.filters;

import com.Life_flow.api_gateway.service.JwtService;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final JwtService jwtService;

    public AuthenticationFilter(JwtService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            log.info("Incoming request: {}", request.getURI());

            String requestPath = request.getURI().getPath();
            if (requestPath.contains("/auth")
                    || requestPath.contains("swagger-ui.html")
                    || requestPath.contains("/v3/api-docs")
                    || requestPath.contains("index.html")
                    || requestPath.contains("/users/swagger-ui")
            ) {

                return chain.filter(exchange);
            }


            final String tokenHeader = request.getHeaders().getFirst("Authorization");

            if (tokenHeader == null || !tokenHeader.startsWith("Bearer")) {
                return handleUnauthorized(exchange, "Authorization token header not found");
            }

            final String token = tokenHeader.substring(7);

            try {
                String userId = jwtService.getUserIdFromToken(token);
                ServerHttpRequest modifiedRequest = request.mutate()
                        .header("X-User-Id", userId)
                        .build();
                ServerWebExchange modifiedExchange = exchange.mutate().request(modifiedRequest).build();
                return chain.filter(modifiedExchange);
            } catch (JwtException e) {
                return handleUnauthorized(exchange, "JWT Exception: " + e.getLocalizedMessage());
            }
        };
    }

    private Mono<Void> handleUnauthorized(ServerWebExchange exchange, String message) {
        log.error(message);
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    public static class Config {

    }
}
