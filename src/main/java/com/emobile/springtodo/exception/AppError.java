package com.emobile.springtodo.exception;

public record AppError(
        int statusCode,
        String message
) {
}
