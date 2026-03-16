package com.emobile.springtodo.mapper;

import com.emobile.springtodo.entity.Task;
import com.emobile.springtodo.entity.TaskStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class TaskRowMapper implements RowMapper<Task> {

    @Override
    public Task mapRow(ResultSet resultSet, int RowNum)
            throws
            SQLException {

        Timestamp createdTs = resultSet.getTimestamp("time_created");
        LocalDateTime timeCreated = createdTs.toLocalDateTime();
        Timestamp updatedTs = resultSet.getTimestamp("time_updated");
        LocalDateTime timeUpdated = updatedTs != null ? updatedTs.toLocalDateTime() : null;
        return Task.builder()
                .id(resultSet.getObject("id", UUID.class))
                .name(resultSet.getString("name"))
                .author(resultSet.getString("author"))
                .description(resultSet.getString("description"))
                .worker(resultSet.getString("worker"))
                .status(TaskStatus.valueOf(resultSet.getString("status")))
                .timeCreated(timeCreated)
                .timeUpdated(timeUpdated)
                .build();
    }
}
