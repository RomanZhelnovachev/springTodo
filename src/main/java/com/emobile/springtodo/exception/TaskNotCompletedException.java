package com.emobile.springtodo.exception;

import java.util.UUID;

public class TaskNotCompletedException extends RuntimeException {
    public TaskNotCompletedException(UUID id) {
        super("Задача с ID " + id + " не выполнена. Не может быть удалена.");
    }
}
