package com.emobile.springtodo.controller;

import com.emobile.springtodo.dto.AppointWorkerRequest;
import com.emobile.springtodo.dto.CreateTaskRequest;
import com.emobile.springtodo.dto.PageableRequest;
import com.emobile.springtodo.dto.TaskDto;
import com.emobile.springtodo.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TaskController implements SwaggerTaskController{

    private final TaskService service;

    @PostMapping("/create")
    public TaskDto createTask(@RequestBody @Valid CreateTaskRequest request) {
        return service.createTask(request);
    }

    @GetMapping("/{id}")
    public TaskDto getTask(@PathVariable UUID id) {
        return service.findTaskById(id);
    }

    @GetMapping()
    public Page<TaskDto> getAll(@RequestParam int limit, @RequestParam int offset){
        return service.findAllTask(new PageableRequest(limit, offset));
    }

    @PatchMapping("/working")
    public TaskDto sendToWork(@RequestBody @Valid AppointWorkerRequest request){
        return service.giveToWork(request);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable UUID id){
        service.deleteTask(id);
    }
}
