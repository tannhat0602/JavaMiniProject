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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private User normalUser;
    private User adminUser;
    private User otherUser;
    private Task task;

    @BeforeEach
    void setUp() {
        normalUser = new User();
        normalUser.setUsername("user1");
        normalUser.setEmail("user1@test.com");
        normalUser.setPassword("pass");
        normalUser.setRole("ROLE_USER");
        ReflectionTestUtils.setField(normalUser, "id", 1L);

        adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setEmail("admin@test.com");
        adminUser.setPassword("pass");
        adminUser.setRole("ROLE_ADMIN");
        ReflectionTestUtils.setField(adminUser, "id", 2L);

        otherUser = new User();
        otherUser.setUsername("user2");
        otherUser.setEmail("user2@test.com");
        otherUser.setPassword("pass");
        otherUser.setRole("ROLE_USER");
        ReflectionTestUtils.setField(otherUser, "id", 3L);

        task = new Task("Test Task", "Description", "TODO", "HIGH", normalUser);
        ReflectionTestUtils.setField(task, "id", 10L);
    }

    @Test
    void createTask_success() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("New Task");
        request.setDescription("Desc");
        request.setStatus("TODO");
        request.setPriority("LOW");

        Task saved = new Task("New Task", "Desc", "TODO", "LOW", normalUser);
        ReflectionTestUtils.setField(saved, "id", 1L);

        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(normalUser));
        when(taskRepository.save(any(Task.class))).thenReturn(saved);

        TaskResponse response = taskService.createTask(request, "user1");

        assertThat(response.getTitle()).isEqualTo("New Task");
        assertThat(response.getUsername()).isEqualTo("user1");
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void createTask_userNotFound_throwsResourceNotFoundException() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Task");
        request.setStatus("TODO");
        request.setPriority("LOW");

        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.createTask(request, "unknown"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void getTaskById_asOwner_success() {
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(normalUser));
        when(taskRepository.findById(10L)).thenReturn(Optional.of(task));

        TaskResponse response = taskService.getTaskById(10L, "user1");

        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getTitle()).isEqualTo("Test Task");
    }

    @Test
    void getTaskById_asAdmin_canAccessAnyTask() {
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(adminUser));
        when(taskRepository.findById(10L)).thenReturn(Optional.of(task));

        TaskResponse response = taskService.getTaskById(10L, "admin");

        assertThat(response.getId()).isEqualTo(10L);
    }

    @Test
    void getTaskById_asOtherUser_throwsUnauthorizedException() {
        when(userRepository.findByUsername("user2")).thenReturn(Optional.of(otherUser));
        when(taskRepository.findById(10L)).thenReturn(Optional.of(task));

        assertThatThrownBy(() -> taskService.getTaskById(10L, "user2"))
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void getTaskById_taskNotFound_throwsResourceNotFoundException() {
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(normalUser));
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTaskById(99L, "user1"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Task not found");
    }

    @Test
    void updateTask_asOwner_success() {
        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setTitle("Updated Title");
        request.setDescription("New desc");
        request.setStatus("IN_PROGRESS");
        request.setPriority("MEDIUM");

        Task updated = new Task("Updated Title", "New desc", "IN_PROGRESS", "MEDIUM", normalUser);
        ReflectionTestUtils.setField(updated, "id", 10L);

        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(normalUser));
        when(taskRepository.findById(10L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(updated);

        TaskResponse response = taskService.updateTask(10L, request, "user1");

        assertThat(response.getTitle()).isEqualTo("Updated Title");
        assertThat(response.getStatus()).isEqualTo("IN_PROGRESS");
    }

    @Test
    void updateTask_asOtherUser_throwsUnauthorizedException() {
        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setTitle("Hacked");
        request.setStatus("DONE");
        request.setPriority("LOW");

        when(userRepository.findByUsername("user2")).thenReturn(Optional.of(otherUser));
        when(taskRepository.findById(10L)).thenReturn(Optional.of(task));

        assertThatThrownBy(() -> taskService.updateTask(10L, request, "user2"))
                .isInstanceOf(UnauthorizedException.class);

        verify(taskRepository, never()).save(any());
    }

    @Test
    void deleteTask_asOwner_success() {
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(normalUser));
        when(taskRepository.findById(10L)).thenReturn(Optional.of(task));

        taskService.deleteTask(10L, "user1");

        verify(taskRepository).delete(task);
    }

    @Test
    void deleteTask_asAdmin_canDeleteAnyTask() {
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(adminUser));
        when(taskRepository.findById(10L)).thenReturn(Optional.of(task));

        taskService.deleteTask(10L, "admin");

        verify(taskRepository).delete(task);
    }

    @Test
    void deleteTask_asOtherUser_throwsUnauthorizedException() {
        when(userRepository.findByUsername("user2")).thenReturn(Optional.of(otherUser));
        when(taskRepository.findById(10L)).thenReturn(Optional.of(task));

        assertThatThrownBy(() -> taskService.deleteTask(10L, "user2"))
                .isInstanceOf(UnauthorizedException.class);

        verify(taskRepository, never()).delete(any());
    }
}
