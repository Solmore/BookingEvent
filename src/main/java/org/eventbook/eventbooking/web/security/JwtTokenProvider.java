package org.eventbook.eventbooking.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.eventbook.eventbooking.domain.exception.AccessDeniedException;
import org.eventbook.eventbooking.service.props.JwtProperties;
import org.eventbook.eventbooking.web.dto.auth.CredentialsResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;
    private final JwtProperties jwtProperties;
    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String createToken(final Long userId,
                              final String email) {
        Claims claims = Jwts.claims()
                .subject(email)
                .add("id", userId)
                .build();
        Instant validity = Instant.now().plus(jwtProperties.getToken(),
                ChronoUnit.HOURS);
        return Jwts.builder()
                .claims(claims)
                .expiration(Date.from(validity))
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(final Long userId,
                                     final String email) {
        Claims claims = Jwts.claims()
                .subject(email)
                .add("id", userId)
                .build();
        Instant validity = Instant.now().plus(jwtProperties.getRefresh(),
                ChronoUnit.DAYS);
        return Jwts.builder()
                .claims(claims)
                .expiration(Date.from(validity))
                .signWith(key)
                .compact();
    }

    public CredentialsResponse refreshToken(final String refreshToken) {
        CredentialsResponse credentialsResponse = new CredentialsResponse();
        if (!isValid(refreshToken)) {
            throw new AccessDeniedException();
        }
        Long userId = getId(refreshToken);
        String email = getEmail(refreshToken);
        credentialsResponse.setToken(createToken(userId, email));
        credentialsResponse.setRefreshToken(createRefreshToken(userId, email));
        return credentialsResponse;
    }

    public boolean isValid(
            final String token
    ) {
        Jws<Claims> claims = Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
        return claims.getPayload()
                .getExpiration()
                .after(new Date());
    }


    public boolean validateToken(final String token) {
        Jws<Claims> claims = Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
        return claims.getPayload().getExpiration().after(new Date());
    }


    public Authentication getAuthentication(final String token) {
        String email = getEmail(token);
        UserDetails userDetails
                = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                "",
                userDetails.getAuthorities());
    }

    private String getEmail(final String token) {
        return Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    private Long getId(
            final String token
    ) {
        return Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("id", Long.class);
    }


}
