package com.example.taskmanagement.dto.requests;

import com.example.taskmanagement.domain.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTaskRequest(
        @NotBlank String title,
        String description,
        @NotNull Priority priority
) {
}
