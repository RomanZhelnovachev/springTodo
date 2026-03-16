package com.emobile.springtodo.dto;

import com.emobile.springtodo.entity.TaskStatus;

import java.util.UUID;

public record TaskDto(
        UUID id,
        String name,
        String author,
        String description,
        String worker,
        TaskStatus status
) {
}
