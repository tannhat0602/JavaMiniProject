package com.example.taskmanagement.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskmanagement.user.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long>{
    Optional<Task> findByTitle(String title);
    boolean existsByTitle(String title);
    boolean existsByDescription(String description);
    boolean existsByStatus(String status);

    

}
