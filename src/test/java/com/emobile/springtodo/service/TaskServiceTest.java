package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.AppointWorkerRequest;
import com.emobile.springtodo.dto.CreateTaskRequest;
import com.emobile.springtodo.dto.TaskDto;
import com.emobile.springtodo.entity.Task;
import com.emobile.springtodo.entity.TaskStatus;
import com.emobile.springtodo.exception.TaskNotCompletedException;
import com.emobile.springtodo.mapper.TaskMapper;
import com.emobile.springtodo.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тестирование TaskService")
class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @Mock
    private TaskMapper mapper;

    @InjectMocks
    private TaskService service;

    private UUID id;
    private String name;
    private String author;
    private String description;
    private String worker;
    private Task task;
    private TaskDto dto;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        name = "Тестирование";
        author = "Иван";
        description = "Тестирование проекта";
        worker = "Петров";
        task = Task.builder()
                .id(id)
                .name("Тестовая задача")
                .author("Иван")
                .description("Описание задачи")
                .status(TaskStatus.TODO)
                .timeCreated(LocalDateTime.now())
                .build();

        dto = new TaskDto(
                task.getId(),
                task.getName(),
                task.getAuthor(),
                task.getDescription(),
                task.getWorker(),
                task.getStatus()
        );
    }

    @Test
    @DisplayName("Создание новой задачи")
    void testCreateTask() {
        CreateTaskRequest request = new CreateTaskRequest(name,
                author,
                description);
        when(mapper.mapToDto(any(Task.class))).thenReturn(dto);
        when(repository.save(any(Task.class))).thenReturn(task);
        TaskDto result = service.createTask(request);
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Поиск задачи по ID")
    void findTaskById() {
        when(mapper.mapToDto(any(Task.class))).thenReturn(dto);
        when(repository.findById(id)).thenReturn(Optional.of(task));
        TaskDto result = service.findTaskById(id);
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.name()).isEqualTo("Тестовая задача");
        assertThat(result.author()).isEqualTo("Иван");
    }

    @Test
    @DisplayName("Передача задачи в работу")
    void testGiveToWork() {
        AppointWorkerRequest request = new AppointWorkerRequest(id, worker);
        when(mapper.mapToDto(any(Task.class))).thenAnswer(invocation -> {
            Task t = invocation.getArgument(0);
            return new TaskDto(t.getId(), t.getName(), t.getAuthor(), t.getDescription(), t.getWorker(), t.getStatus());
        });
        when(repository.findById(id)).thenReturn(Optional.of(task));
        doNothing().when(repository).updateTask(any(Task.class));
        TaskDto result = service.giveToWork(request);
        assertThat(result.worker()).isEqualTo(worker);
        assertThat(result.status()).isEqualTo(TaskStatus.IN_WORK);
    }

    @Test
    @DisplayName("Если задача не выполнена, то при её удалении выбрасывается исключение")
    void testIncorrectDeleteTask() {
        when(repository.findById(id)).thenReturn(Optional.of(task));
        assertThrows(TaskNotCompletedException.class, ()-> service.deleteTask(id));
    }
}