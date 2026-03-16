package com.emobile.springtodo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundByIdException.class)
    public ResponseEntity<AppError> handleNotFound(TaskNotFoundByIdException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new AppError(HttpStatus.NOT_FOUND.value(),
                        e.getMessage()));
    }

    @ExceptionHandler(TaskNotCompletedException.class)
    public ResponseEntity<AppError> handleBadRequest(TaskNotCompletedException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new AppError(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }
}
