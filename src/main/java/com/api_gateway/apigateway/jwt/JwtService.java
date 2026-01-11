package com.api_gateway.apigateway.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtService {
    @Value("${token.signing.key}")
    private String secretKey;


    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public Mono<Claims> validateToken(String token) {
        return Mono.fromCallable(() -> {Claims claims = Jwts.parser()
        .setSigningKey(getSigningKey())
                .build()
        .parseClaimsJws(token)
        .getBody();

        Date expired = claims.getExpiration();
        if (expired.before(new Date()) || expired == null) {
        throw new JwtException("Expired or invalid JWT token");}
        return claims;
        });
    }
}
