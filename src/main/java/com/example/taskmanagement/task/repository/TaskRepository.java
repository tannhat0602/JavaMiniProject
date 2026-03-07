package com.example.taskmanagement.task.repository;

import com.example.taskmanagement.task.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByUserId(Long userId, Pageable pageable);

    Page<Task> findByUserIdAndStatus(Long userId, String status, Pageable pageable);

    Page<Task> findByUserIdAndPriority(Long userId, String priority, Pageable pageable);

    Page<Task> findByUserIdAndStatusAndPriority(Long userId, String status, String priority, Pageable pageable);

    Optional<Task> findByIdAndUserId(Long id, Long userId);

    // ADMIN queries — filter toàn bộ tasks không giới hạn userId
    Page<Task> findByStatus(String status, Pageable pageable);

    Page<Task> findByPriority(String priority, Pageable pageable);

    Page<Task> findByStatusAndPriority(String status, String priority, Pageable pageable);
}
