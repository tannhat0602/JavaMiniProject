package com.example.taskmanagement.notification;

import com.example.taskmanagement.task.entity.Task;
import com.example.taskmanagement.task.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class TaskReminderScheduler {

    private static final Logger log = LoggerFactory.getLogger(TaskReminderScheduler.class);

    private final TaskRepository taskRepository;
    private final EmailService emailService;

    public TaskReminderScheduler(TaskRepository taskRepository, EmailService emailService) {
        this.taskRepository = taskRepository;
        this.emailService = emailService;
    }

    // Chạy mỗi giờ
    @Scheduled(fixedRate = 3600000)
    public void checkUpcomingDeadlines() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime in24h = now.plusHours(24);

        List<Task> tasks = taskRepository.findTasksWithDeadlineBetween(now, in24h);

        if (tasks.isEmpty()) {
            log.debug("No upcoming deadlines found.");
            return;
        }

        log.info("Found {} task(s) with deadline in the next 24 hours. Sending reminders...", tasks.size());

        for (Task task : tasks) {
            try {
                emailService.sendDeadlineReminder(task);
            } catch (Exception e) {
                log.error("Failed to send reminder for task id={}: {}", task.getId(), e.getMessage());
            }
        }
    }
}
