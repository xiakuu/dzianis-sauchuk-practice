package com.api_gateway.apigateway.controller;

import com.api_gateway.apigateway.model.SignUpRequest;
import com.api_gateway.apigateway.model.SignUpResponse;
import com.api_gateway.apigateway.model.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class ApiGatewayController {
    private final WebClient authClient;
    private final WebClient userClient;

    @PostMapping("/sign-up")
    public Mono<ResponseEntity<String>> register(@RequestBody SignUpRequest signUpRequest) {
        return authClient.post()
                .uri("/auth/sign-up")
                .bodyValue(signUpRequest)
                .retrieve()
                .bodyToMono(SignUpResponse.class)
                .flatMap(signUpResponse ->
                    userClient.post()
                            .uri("/api/users/create")
                            .bodyValue(signUpRequest)
                            .retrieve()
                            .bodyToMono(UserResponse.class)
                            .onErrorResume(ex -> onError(signUpResponse.userId(), HttpStatus.UNAUTHORIZED).then(Mono.error(new RuntimeException("Registration error, rollback"))))
                )
                .map(userResult -> ResponseEntity.status(HttpStatus.CREATED).body(userResult.toString()));
    }

    private Mono<Void> onError(Long userId, HttpStatus status) {
        return authClient.delete()
                .uri("/api/users/delete/{id}", userId)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(log -> System.out.println("rolled back for user " + userId + " with status " + status));

    }

}
