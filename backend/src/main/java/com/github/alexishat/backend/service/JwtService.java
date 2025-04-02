package com.github.alexishat.backend.service;

import com.github.alexishat.backend.dtos.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;
import java.util.Date;


@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    String jwtSecret;

    @Value("${jwt.expiration}")
    Long jwtExpirationTime;

    Key key;

    private final UserService userService;

    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtExpirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            UserDto user = userService.findByLogin(username);

            return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
        } catch (JwtException e) {
            throw new RuntimeException("Invalid JWT token");
        }
    }
}
