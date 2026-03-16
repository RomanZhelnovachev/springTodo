package com.emobile.springtodo.repository;

import com.emobile.springtodo.config.PostgresTestContainer;
import com.emobile.springtodo.entity.Task;
import com.emobile.springtodo.entity.TaskStatus;
import com.emobile.springtodo.mapper.TaskRowMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJdbcTest
@Import({PostgresTestContainer.class, TaskRepository.class, TaskRowMapper.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Интеграционное тестирование TaskRepository")
class TaskRepositoryTest {

    @Autowired
    private TaskRepository repository;

    @DynamicPropertySource
    static void config(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", PostgresTestContainer.container::getJdbcUrl);
        registry.add("spring.datasource.username", PostgresTestContainer.container::getUsername);
        registry.add("spring.datasource.password", PostgresTestContainer.container::getPassword);
    }

    @Test
    @DisplayName("Сохранение и поиск задачи")
    @Transactional
    @Sql(scripts = "/sql/clean-tasks.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testSaveAndFindTask() {
        UUID id = UUID.randomUUID();
        Task task = Task.builder()
                .id(id)
                .name("Testing")
                .author("Ivan")
                .description("Тестирование проекта")
                .status(TaskStatus.TODO)
                .timeCreated(LocalDateTime.now())
                .build();

        repository.save(task);
        Optional<Task> found = repository.findById(id);

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Testing");
    }

    @Test
    @DisplayName("Удаление задачи")
    @Transactional
    @Sql(scripts = {
            "/sql/clean-tasks.sql",
            "/sql/insert-test-task.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testDeleteTask() {
        UUID taskId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        repository.deleteTask(taskId);

        assertThrows(
                EmptyResultDataAccessException.class,
                () -> repository.findById(taskId).orElseThrow()
        );
    }
}