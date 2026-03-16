package com.example.customersupporthub.security;

import com.example.customersupporthub.domain.Role;

public record AppUserPrincipal(
        Long id,
        String username,
        Role role
) {
    public boolean isAdmin() {
        return role == Role.ADMIN;
    }
}
