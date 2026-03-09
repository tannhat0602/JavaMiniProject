package com.example.taskmanagement.task.repository;

import com.example.taskmanagement.task.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByUserId(Long userId, Pageable pageable);

    Page<Task> findByUserIdAndStatus(Long userId, String status, Pageable pageable);

    Page<Task> findByUserIdAndPriority(Long userId, String priority, Pageable pageable);

    Page<Task> findByUserIdAndStatusAndPriority(Long userId, String status, String priority, Pageable pageable);

    Optional<Task> findByIdAndUserId(Long id, Long userId);

    // ADMIN queries
    Page<Task> findByStatus(String status, Pageable pageable);

    Page<Task> findByPriority(String priority, Pageable pageable);

    Page<Task> findByStatusAndPriority(String status, String priority, Pageable pageable);

    // Deadline reminder queries
    @Query("SELECT t FROM Task t WHERE t.deadline BETWEEN :from AND :to AND t.status != 'DONE'")
    List<Task> findTasksWithDeadlineBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND t.deadline BETWEEN :from AND :to AND t.status != 'DONE'")
    List<Task> findTasksWithDeadlineBetweenForUser(@Param("userId") Long userId, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
