package com.emobile.springtodo.controller;

import com.emobile.springtodo.dto.AppointWorkerRequest;
import com.emobile.springtodo.dto.CreateTaskRequest;
import com.emobile.springtodo.dto.TaskDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Tag(name = "Task controller", description = "Контроллер для сервиса задач")
public interface SwaggerTaskController {

    @Operation(summary = "Создание задачи", description = "Создаёт новую задачу")
    TaskDto createTask(@Valid @RequestBody CreateTaskRequest request);

    @Operation(summary = "Получить задачу по ID")
    TaskDto getTask(@PathVariable UUID id);

    @Operation(summary = "Получить постраничный список задач")
    Page<TaskDto> getAll(@RequestParam int limit, @RequestParam int offset);

    @Operation(summary = "Назначить исполнителя")
    TaskDto sendToWork(@Valid @RequestBody AppointWorkerRequest request);

    @Operation(summary = "Удалить выполненную задачу")
    void deleteTask(@PathVariable UUID id);
}
