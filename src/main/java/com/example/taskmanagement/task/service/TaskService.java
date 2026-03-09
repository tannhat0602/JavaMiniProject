package com.example.taskmanagement.task.service;

import com.example.taskmanagement.task.dto.CreateTaskRequest;
import com.example.taskmanagement.task.dto.TaskResponse;
import com.example.taskmanagement.task.dto.UpdateTaskRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {

    TaskResponse createTask(CreateTaskRequest request, String username);

    Page<TaskResponse> getAllTasks(String username, String status, String priority, Pageable pageable);

    TaskResponse getTaskById(Long taskId, String username);

    TaskResponse updateTask(Long taskId, UpdateTaskRequest request, String username);

    void deleteTask(Long taskId, String username);

    List<TaskResponse> getUpcomingTasks(String username);
}
