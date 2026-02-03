package com.example.taskmanagement.service;

import com.example.taskmanagement.dto.TaskDto;
import com.example.taskmanagement.dto.TaskMapper;
import com.example.taskmanagement.exception.DuplicateTaskForDayException;
import com.example.taskmanagement.domain.Priority;
import com.example.taskmanagement.domain.Status;
import com.example.taskmanagement.domain.TaskEntity;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.dto.requests.CreateTaskRequest;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

public class TaskManagementServiceTest {

    @Test
    void duplicate_shouldThrowDuplicateTaskForDayException() {
        TaskRepository repo = mock(TaskRepository.class);
        TaskMapper mapper = mock(TaskMapper.class);

        when(repo.saveAndFlush(any(TaskEntity.class)))
                .thenThrow(new DataIntegrityViolationException("uq violation"));

        TaskManagementService service = new TaskManagementService(mapper, repo);

        assertThrows(DuplicateTaskForDayException.class, () ->
                service.createTask(new CreateTaskRequest("Buy milk", "x", Priority.HIGH)));
    }

    @Test
    void getAllUrgent_returnsDtosInRepoOrder() {
        TaskRepository repo = mock(TaskRepository.class);
        TaskMapper mapper = mock(TaskMapper.class);

        TaskEntity e1 = new TaskEntity("H-old", "d", Priority.HIGH);
        TaskEntity e2 = new TaskEntity("H-new", "d", Priority.HIGH);

        when(repo.findPendingOrderByPriorityAndCreatedAt()).thenReturn(List.of(e1, e2));
        when(mapper.toDto(e1)).thenReturn(new TaskDto(1L, "H-old", "d", Priority.HIGH, Status.PENDING, Instant.now(), null));
        when(mapper.toDto(e2)).thenReturn(new TaskDto(2L, "H-new", "d", Priority.HIGH, Status.PENDING, Instant.now(), null));

        TaskManagementService service = new TaskManagementService(mapper, repo);

        List<TaskDto> result = service.getAllUrgent();

        assertThat(result).extracting(TaskDto::title).containsExactly("H-old", "H-new");
        verify(repo).findPendingOrderByPriorityAndCreatedAt();
    }
}
