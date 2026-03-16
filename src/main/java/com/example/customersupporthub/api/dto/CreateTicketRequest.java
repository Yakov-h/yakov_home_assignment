package com.example.customersupporthub.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTicketRequest(
        @NotBlank @Size(max = 150) String subject,
        @NotBlank @Size(max = 2000) String description
) {
}
