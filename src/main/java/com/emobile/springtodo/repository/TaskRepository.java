package com.emobile.springtodo.repository;

import com.emobile.springtodo.entity.Task;
import com.emobile.springtodo.mapper.TaskRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TaskRepository {

    private final JdbcTemplate template;
    private final TaskRowMapper mapper;

    public Task save(Task task){
        String sql = "INSERT INTO tasks " + "(id, name, author, description, worker, status, time_created, time_updated) " + "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        template.update(sql,
                task.getId(),
                task.getName(),
                task.getAuthor(),
                task.getDescription(),
                task.getWorker(),
                task.getStatus()
                        .name(),
                Timestamp.valueOf(task.getTimeCreated()),
                task.getTimeUpdated() != null ?
                        Timestamp.valueOf(task.getTimeUpdated()) :
                        null);
        return task;
    }

    public Optional<Task> findById(UUID id){
        String sql = "SELECT * FROM tasks WHERE id = ?";
        return Optional.ofNullable(template.queryForObject(sql,
                mapper,
                id));
    }

    public Page<Task> findAll(Pageable pageable){
        String sql = "SELECT * FROM tasks LIMIT ? OFFSET ?";
        List<Task> tasks = template.query(sql,
                mapper, pageable.getPageSize(), pageable.getOffset());
        String countSql = "SELECT COUNT(*) FROM tasks";
        long totalElements = template.queryForObject(countSql, Long.class);
        return new PageImpl<>(tasks, pageable, totalElements);
    }

    public void updateTask(Task task){
        String sql = "UPDATE tasks SET worker = ?, status = ?, time_updated = ? WHERE id = ?";
        template.update(sql, task.getWorker(), task.getStatus().name(), task.getTimeUpdated(), task.getId());
    }

    public void deleteTask(UUID id){
        String sql = "DELETE FROM tasks WHERE id = ?";
       template.update(sql, id);
    }

    public void deleteAll() {
        String sql = "DELETE FROM tasks";
        template.update(sql);
    }
}
