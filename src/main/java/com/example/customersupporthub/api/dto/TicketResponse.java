package com.example.customersupporthub.api.dto;

import com.example.customersupporthub.domain.TicketStatus;

import java.time.Instant;

public record TicketResponse(
        Long id,
        String subject,
        String description,
        TicketStatus status,
        Long customerId,
        String customerUsername,
        Instant createdAt,
        Instant updatedAt
) {
}
