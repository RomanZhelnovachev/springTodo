package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.*;
import com.emobile.springtodo.entity.Task;
import com.emobile.springtodo.entity.TaskStatus;
import com.emobile.springtodo.exception.TaskNotCompletedException;
import com.emobile.springtodo.exception.TaskNotFoundByIdException;
import com.emobile.springtodo.mapper.TaskMapper;
import com.emobile.springtodo.repository.TaskRepository;
import com.emobile.springtodo.repository.TaskRepositoryHibernate;
import com.emobile.springtodo.repository.TaskRepositoryJPA;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

//    private final TaskRepository repository;
//    private final TaskRepositoryHibernate repository;
    private final TaskRepositoryJPA repository;
    private final TaskMapper mapper;
    private final MeterRegistry registry;

    @Transactional
    public TaskDto createTask(CreateTaskRequest request) {
        Task task = Task.builder()
                .id(UUID.randomUUID())
                .name(request.name())
                .author(request.author())
                .description(request.description())
                .status(TaskStatus.TODO)
                .timeCreated(LocalDateTime.now())
                .build();
        log.info("Создана новая задача {}", task.getName());
        repository.save(task);
        return mapper.mapToDto(task);
    }

    @Cacheable(value = "tasks", key = "#id")
    @Transactional(readOnly = true)
    public TaskDto findTaskById(UUID id) {
        log.info("Задача с ID {} найдена", id);
        return mapper.mapToDto(getTaskById(id));
    }

    @Transactional(readOnly = true)
    public Page<TaskDto> findAllTask(PageableRequest request) {
        log.info("Показан постраничный список всех задач");
        return repository.findAll(request.toPageable())
                .map(mapper::mapToDto);
    }

    @CacheEvict(value = "tasks", key = "#request.id()")
    @Transactional
    public TaskDto giveToWork(AppointWorkerRequest request) {
        Task task = getTaskById(request.id());
        task.setWorker(request.worker());
        task.setStatus(TaskStatus.IN_WORK);
        task.setTimeUpdated(LocalDateTime.now());
//        repository.updateTask(task);
        log.info("Задача с ID {} отправлена в работу", task.getId());
        return mapper.mapToDto(task);
    }

    @CacheEvict(value = "tasks", key = "#id")
    @Transactional
    public void deleteTask(UUID id){
        Task task = getTaskById(id);
        if(task.getStatus() == TaskStatus.DONE){
            log.info("Задача с ID {} выполнена и удалена", id);
            registry.counter("task.completed").increment();
//            repository.deleteTask(id);
            repository.deleteById(id);
        } else {
            log.error("Задача с ID {} ещё не выполнена", id);
            throw new TaskNotCompletedException(id);
        }
    }

    private Task getTaskById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundByIdException(id));
    }
}
