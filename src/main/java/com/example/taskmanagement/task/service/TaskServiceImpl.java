package com.example.taskmanagement.task.service;

import com.example.taskmanagement.exception.ResourceNotFoundException;
import com.example.taskmanagement.exception.UnauthorizedException;
import com.example.taskmanagement.task.dto.CreateTaskRequest;
import com.example.taskmanagement.task.dto.TaskResponse;
import com.example.taskmanagement.task.dto.UpdateTaskRequest;
import com.example.taskmanagement.task.entity.Task;
import com.example.taskmanagement.task.repository.TaskRepository;
import com.example.taskmanagement.user.entity.User;
import com.example.taskmanagement.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TaskResponse createTask(CreateTaskRequest request, String username) {
        User user = findUserByUsername(username);

        Task task = new Task(
                request.getTitle(),
                request.getDescription(),
                request.getStatus(),
                request.getPriority(),
                user
        );

        TaskResponse response = toResponse(taskRepository.save(task));
        log.info("Task created: id={} by user={}", response.getId(), username);
        return response;
    }

    @Override
    public Page<TaskResponse> getAllTasks(String username, String status, String priority, Pageable pageable) {
        User user = findUserByUsername(username);

        // ADMIN thấy tất cả tasks, USER chỉ thấy tasks của mình
        if (isAdmin(user)) {
            return getAllTasksForAdmin(status, priority, pageable);
        }

        return getAllTasksForUser(user.getId(), status, priority, pageable);
    }

    @Override
    public TaskResponse getTaskById(Long taskId, String username) {
        User user = findUserByUsername(username);
        Task task = findTaskById(taskId);
        if (!isAdmin(user)) {
            checkOwnership(task, user);
        }
        return toResponse(task);
    }

    @Override
    public TaskResponse updateTask(Long taskId, UpdateTaskRequest request, String username) {
        User user = findUserByUsername(username);
        Task task = findTaskById(taskId);
        if (!isAdmin(user)) {
            checkOwnership(task, user);
        }

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());

        TaskResponse response = toResponse(taskRepository.save(task));
        log.info("Task updated: id={} by user={}", taskId, username);
        return response;
    }

    @Override
    public void deleteTask(Long taskId, String username) {
        User user = findUserByUsername(username);
        Task task = findTaskById(taskId);
        if (!isAdmin(user)) {
            checkOwnership(task, user);
        }
        taskRepository.delete(task);
        log.info("Task deleted: id={} by user={}", taskId, username);
    }

    // ===== Private helpers =====

    private boolean isAdmin(User user) {
        return "ROLE_ADMIN".equals(user.getRole());
    }

    private Page<TaskResponse> getAllTasksForAdmin(String status, String priority, Pageable pageable) {
        Page<Task> tasks;
        if (status != null && priority != null) {
            tasks = taskRepository.findByStatusAndPriority(status, priority, pageable);
        } else if (status != null) {
            tasks = taskRepository.findByStatus(status, pageable);
        } else if (priority != null) {
            tasks = taskRepository.findByPriority(priority, pageable);
        } else {
            tasks = taskRepository.findAll(pageable);
        }
        return tasks.map(this::toResponse);
    }

    private Page<TaskResponse> getAllTasksForUser(Long userId, String status, String priority, Pageable pageable) {
        Page<Task> tasks;
        if (status != null && priority != null) {
            tasks = taskRepository.findByUserIdAndStatusAndPriority(userId, status, priority, pageable);
        } else if (status != null) {
            tasks = taskRepository.findByUserIdAndStatus(userId, status, pageable);
        } else if (priority != null) {
            tasks = taskRepository.findByUserIdAndPriority(userId, priority, pageable);
        } else {
            tasks = taskRepository.findByUserId(userId, pageable);
        }
        return tasks.map(this::toResponse);
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    private Task findTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
    }

    private void checkOwnership(Task task, User user) {
        if (!task.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("You do not have permission to access this task");
        }
    }

    private TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getUser().getId(),
                task.getUser().getUsername(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
