package com.example.customersupporthub.security;

import com.example.customersupporthub.domain.Role;
import com.example.customersupporthub.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    public AppUserPrincipal currentUser(Authentication authentication) {
        if (!(authentication instanceof JwtAuthenticationToken jwtAuth)) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Authentication is required.");
        }
        return new AppUserPrincipal(
                jwtAuth.getToken().getClaim("userId"),
                jwtAuth.getName(),
                Role.valueOf(jwtAuth.getToken().getClaimAsString("role"))
        );
    }
}
