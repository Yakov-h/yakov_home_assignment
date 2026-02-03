package com.example.taskmanagement.controller;

import com.example.taskmanagement.dto.TaskDto;
import com.example.taskmanagement.dto.requests.CreateTaskRequest;
import com.example.taskmanagement.dto.requests.UpdateTaskRequest;
import com.example.taskmanagement.service.TaskManagementService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@Validated
public class TaskManagementController {

    private final TaskManagementService service;

    public TaskManagementController(TaskManagementService service) {
        this.service = service;
    }

    //Create a new task
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto createTask(@RequestBody @Valid CreateTaskRequest createTaskRequest) {
        return service.createTask(createTaskRequest);
    }

    //Retrieve all tasks
    @GetMapping
    public List<TaskDto> getAll() {
        return service.getAll();
    }

    //Retrieve all PENDING tasks, sorted by priority (HIGH first) and then by createdAt (oldest first)
    @GetMapping("/urgent")
    public List<TaskDto> getAllUrgent() {
        return service.getAllUrgent();
    }

    //Update task details or status
    @PutMapping("/{id}")
    public TaskDto updateTask(@PathVariable @Positive Long id,@RequestBody @Valid UpdateTaskRequest updateTaskRequest) {
        return service.updateTask(id, updateTaskRequest);
    }

    //Delete a task
    @DeleteMapping("/{id}")
    public TaskDto deleteTask(@PathVariable @Positive Long id) {
        return service.deleteTask(id);
    }

}
