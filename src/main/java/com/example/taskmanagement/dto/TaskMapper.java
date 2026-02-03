package com.example.taskmanagement.dto;

import com.example.taskmanagement.domain.TaskEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskDto toDto(TaskEntity e) {
        return new TaskDto(e.getId(), e.getTitle(), e.getDescription(), e.getPriority(), e.getStatus(), e.getCreatedAt(), e.getCompletedAt());
    }
}
