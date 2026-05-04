package dev.ewerton.todolist.domain.repository;

import dev.ewerton.todolist.domain.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

        List<Task> findByStatus(String status);
        List<Task> findByTitleContainingIgnoreCase(String title);
}
