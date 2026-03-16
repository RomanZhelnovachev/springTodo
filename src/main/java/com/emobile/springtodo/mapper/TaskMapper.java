package com.emobile.springtodo.mapper;

import com.emobile.springtodo.dto.TaskDto;
import com.emobile.springtodo.entity.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task mapToEntity(TaskDto taskDto);

    TaskDto mapToDto(Task task);
}
