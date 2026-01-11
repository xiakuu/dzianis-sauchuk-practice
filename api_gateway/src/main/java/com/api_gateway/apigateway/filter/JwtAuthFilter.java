package com.api_gateway.apigateway.filter;

import com.api_gateway.apigateway.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter implements GlobalFilter {

    private final JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        System.out.println(path + exchange.getRequest());
        if (path.startsWith("/auth/login") || path.startsWith("/auth/register")) {
            return chain.filter(exchange);
        }

        List<String> headers = exchange.getRequest().getHeaders().getOrEmpty("Authorization");
        if (headers.isEmpty() || !headers.get(0).startsWith("Bearer")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        String token = headers.get(0);

        return jwtService.validateToken(token).flatMap(claims -> {
            ServerHttpRequest req = (ServerHttpRequest) exchange.getRequest()
                .mutate()
                .header("X-User-ID", String.valueOf(claims.get("userId")))
                .build();
        return chain.filter(exchange.mutate().request(req).build());
        })
                .onErrorResume(e -> Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalid")));
    }


}
