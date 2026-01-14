package innowise.security.service;

import innowise.security.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import innowise.security.domain.dto.JwtAuthenticationResponse;
import innowise.security.domain.dto.SignInRequest;
import innowise.security.domain.dto.SignUpRequest;
import innowise.security.domain.model.Role;
import innowise.security.domain.model.User;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;


    public void signUp(SignUpRequest request) {
        if(userRepository.existsByUsername(request.getUsername())) {
            throw new BadCredentialsException("Username is already in use");
        }
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();
        userService.create(user);
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        User user1 = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new BadCredentialsException("User " + request.getUsername() + " not found"));
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));
        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());
        var jwtAccess = jwtService.generateToken(user);
        var jwtRefresh = jwtService.generateRefreshToken(user);
        user1.setRefreshToken(jwtRefresh);
        userRepository.save(user1);
        return new JwtAuthenticationResponse(jwtAccess, jwtRefresh);
    }

    public JwtAuthenticationResponse refresh(String refreshToken) {
        try {
            String userName = jwtService.extractUserName(refreshToken);
            User user = userRepository.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException("User " + userName + " not found"));
            if (!refreshToken.equals(user.getRefreshToken())) {
                throw new BadCredentialsException("Invalid refresh token");
            }
            String newJwtAccess = jwtService.generateToken(user);
            String newJwtRefresh = jwtService.generateRefreshToken(user);
            user.setRefreshToken(newJwtRefresh);
            userRepository.save(user);
            return new JwtAuthenticationResponse(newJwtAccess, newJwtRefresh);
        } catch (JwtException e){
            throw new BadCredentialsException("Invalid refresh token");
        }
    }

    public boolean vaildateToken(String token, UserDetails user) {
        try {
            return jwtService.isTokenValid(token, user);
        } catch (JwtException e) {
            return false;
        }
    }
}
