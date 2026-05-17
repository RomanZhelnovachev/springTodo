package com.emobile.springtodo.repository;

import com.emobile.springtodo.entity.Task;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryHibernate {

    private final EntityManager manager;

    public Task save(Task task) {
        manager.persist(task);
        return task;
    }

    public Optional<Task> findById(UUID id){
        Task task = manager.find(Task.class, id);
        return Optional.ofNullable(task);
    }

    public Page<Task> findAll(Pageable pageable) {
        List<Task> tasks = manager.createQuery("FROM Task", Task.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        Long total = manager.createQuery("SELECT COUNT(t) FROM Task t", Long.class)
                .getSingleResult();
        return new PageImpl<>(tasks, pageable, total);
    }

    public void updateTask(Task task) {
        manager.merge(task);
    }

    public void deleteTask(UUID id) {
        Task task = manager.find(Task.class, id);
        if (task != null) {
            manager.remove(task);
        }
    }

    public void deleteAll() {
        manager.createQuery("DELETE FROM Task")
                .executeUpdate();
    }
}
