package com.example.taskmanagement.service;

import com.example.taskmanagement.dto.TaskDto;
import com.example.taskmanagement.dto.TaskMapper;
import com.example.taskmanagement.exception.DuplicateTaskForDayException;
import com.example.taskmanagement.exception.NotFoundException;
import com.example.taskmanagement.domain.Status;
import com.example.taskmanagement.domain.TaskEntity;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.dto.requests.CreateTaskRequest;
import com.example.taskmanagement.dto.requests.UpdateTaskRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskManagementService {

    private final TaskRepository repository;
    private final TaskMapper mapper;

    public TaskManagementService(TaskMapper mapper, TaskRepository repository) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public TaskDto createTask(CreateTaskRequest createTaskRequest){
        TaskEntity taskEntity = new TaskEntity(createTaskRequest.title(), createTaskRequest.description(), createTaskRequest.priority());
        try {
            repository.saveAndFlush(taskEntity);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateTaskForDayException("Task with same title already exists today", e);
        }
        return mapper.toDto(taskEntity);
    }

    @Transactional(readOnly = true)
    public List<TaskDto> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TaskDto> getAllUrgent() {
        return repository.findPendingOrderByPriorityAndCreatedAt()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional
    public TaskDto updateTask(long taskId, UpdateTaskRequest updateTaskRequest){
        TaskEntity taskEntity = repository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found: " + taskId));
        taskEntity.setTitle(updateTaskRequest.title());
        taskEntity.setDescription(updateTaskRequest.description());
        taskEntity.setPriority(updateTaskRequest.priority());
        taskEntity.setStatus(updateTaskRequest.status());

        if (taskEntity.getStatus().equals(Status.DONE)) {
            taskEntity.complete();
        }
        try {
            repository.saveAndFlush(taskEntity);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateTaskForDayException("Task with same title already exists today", e);
        }
        return mapper.toDto(taskEntity);
    }

    @Transactional
    public TaskDto deleteTask(long taskId){
        TaskEntity taskEntity = repository.findById(taskId)
                        .orElseThrow(() -> new NotFoundException("Task not found: " + taskId));
        repository.delete(taskEntity);
        return mapper.toDto(taskEntity);
    }
}
