package com.innowise.orderservice.orderservice.service;

import com.innowise.orderservice.orderservice.model.response.UserResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
    private final WebClient webClient;

    @CircuitBreaker(name = "userService", fallbackMethod = "fallback")
    public UserResponse getUser(Long userId) {
        return webClient.get()
                .uri("api/users/{id}", userId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        error -> Mono.error(new RuntimeException("not found API"))
                )
                .onStatus(HttpStatusCode::is5xxServerError,
                        error -> Mono.error(new RuntimeException("server error"))
                )
                .bodyToMono(UserResponse.class)
                .block();
    }

    public UserResponse fallback(Long id, RuntimeException e) {
        System.out.println("fallback, external api is not available, id " + id + ", " + e.getMessage());
        return new UserResponse();
    }
}
