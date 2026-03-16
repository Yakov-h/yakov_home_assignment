package com.example.customersupporthub.api.dto;

import com.example.customersupporthub.domain.Role;

public record UserResponse(
        Long id,
        String username,
        String fullName,
        String email,
        Role role,
        Long agentId
) {
}
