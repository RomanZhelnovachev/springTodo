package com.emobile.springtodo.repository;

import com.emobile.springtodo.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaskRepositoryJPA extends JpaRepository<Task, UUID> {
}
