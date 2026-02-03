package com.example.taskmanagement.dto;

import com.example.taskmanagement.domain.Priority;
import com.example.taskmanagement.domain.Status;

import java.time.Instant;

public record TaskDto(
        Long id,
        String title,
        String description,
        Priority priority,
        Status status,
        Instant createdAt,
        Instant completedAt
) {
}
