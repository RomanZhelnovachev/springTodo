package com.emobile.springtodo.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Task {

    private UUID id;
    private String name;
    private String author;
    private String description;
    private String worker;
    private TaskStatus status;
    private LocalDateTime timeCreated;
    private LocalDateTime timeUpdated;

}
