package com.emobile.springtodo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AppointWorkerRequest(

        @NotBlank(message = "ID не может быть пустым")
        UUID id,

        @NotBlank(message = "ID не может быть пустым")
        @Size(max = 50, message = "Поле не может превышать 50 символов")
        String worker
) {
}
