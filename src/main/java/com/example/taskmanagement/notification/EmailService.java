package com.example.taskmanagement.notification;

import com.example.taskmanagement.task.entity.Task;

public interface EmailService {
    void sendDeadlineReminder(Task task);
}
