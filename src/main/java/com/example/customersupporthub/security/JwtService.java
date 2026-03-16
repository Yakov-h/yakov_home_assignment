package com.example.customersupporthub.security;

import com.example.customersupporthub.domain.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final long expirationSeconds;

    public JwtService(JwtEncoder jwtEncoder,
                      JwtDecoder jwtDecoder,
                      @Value("${app.security.jwt.expiration-seconds}") long expirationSeconds) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.expirationSeconds = expirationSeconds;
    }

    public String generateToken(Long userId, String username, Role role) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expirationSeconds))
                .subject(username)
                .claims(c -> c.putAll(Map.of(
                        "userId", userId,
                        "role", role.name(),
                        "scope", "ROLE_" + role.name()
                )))
                .build();
        JwsHeader jwsHeader = JwsHeader.with(org.springframework.security.oauth2.jose.jws.MacAlgorithm.HS256)
                .type("JWT")
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    public AppUserPrincipal parse(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        Number userId = jwt.getClaim("userId");
        return new AppUserPrincipal(
                userId.longValue(),
                jwt.getSubject(),
                Role.valueOf(jwt.getClaimAsString("role"))
        );
    }

    public long getExpirationSeconds() {
        return expirationSeconds;
    }
}
