package com.emobile.springtodo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTaskRequest(

        @NotBlank(message = "Имя не может быть пустым")
        @Size(max = 100, message = "Поле не может превышать 100 символов")
        @JsonProperty("name")
        String name,

        @NotBlank(message = "Автор должен быть указан")
        @Size(max = 50, message = "Поле не может превышать 50 символов")
        @JsonProperty("author")
        String author,

        @NotBlank(message = "У задачи должно быть описание")
        @Size(max = 255, message = "Поле не может превышать 255 символов")
        @JsonProperty("description")
        String description) {
}
