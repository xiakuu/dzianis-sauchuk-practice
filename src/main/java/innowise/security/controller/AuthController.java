package innowise.security.controller;

import innowise.security.domain.dto.RefreshRequest;
import innowise.security.repository.UserRepository;
import innowise.security.service.JwtService;
import innowise.security.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import innowise.security.domain.dto.JwtAuthenticationResponse;
import innowise.security.domain.dto.SignInRequest;
import innowise.security.domain.dto.SignUpRequest;
import innowise.security.service.AuthenticationService;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequest request) {
        authenticationService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/sign-in")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody @Valid SignInRequest request) {
        return ResponseEntity.ok(authenticationService.signIn(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshRequest request) {
        return ResponseEntity.ok(authenticationService.refresh(request.refreshToken()));
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestHeader("Authorization") String authHeader) {
        if(!authHeader.startsWith("Bearer ")) {return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();}
        var jwt = authHeader.substring(7);
        var username = jwtService.extractUserName(jwt);
        UserDetails userDetails = userService.userDetailsService().loadUserByUsername(username);
        return ResponseEntity.ok(authenticationService.vaildateToken(jwt, userDetails));

    }
}

