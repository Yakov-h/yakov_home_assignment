package com.example.taskmanagement.dto.requests;

import com.example.taskmanagement.domain.Priority;
import com.example.taskmanagement.domain.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateTaskRequest(
        @NotBlank String title,
        String description,
        @NotNull Priority priority,
        @NotNull Status status
) {
}
