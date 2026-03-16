package com.emobile.springtodo.exception;

import java.util.UUID;

public class TaskNotFoundByIdException extends RuntimeException {
    public TaskNotFoundByIdException(UUID id) {
        super("Задача не найдена по ID " + id);
    }
}
