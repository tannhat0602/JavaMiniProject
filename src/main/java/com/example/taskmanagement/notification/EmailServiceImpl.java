package com.example.taskmanagement.notification;

import com.example.taskmanagement.task.entity.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendDeadlineReminder(Task task) {
        String to = task.getUser().getEmail();
        String subject = "[Task Reminder] \"" + task.getTitle() + "\" is due soon";
        String body = "Hi " + task.getUser().getUsername() + ",\n\n"
                + "Your task \"" + task.getTitle() + "\" is due on " + task.getDeadline() + ".\n"
                + "Current status: " + task.getStatus() + "\n\n"
                + "Please complete it before the deadline.\n\n"
                + "— Task Management System";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
        log.info("Deadline reminder sent to {} for task: {}", to, task.getTitle());
    }
}
