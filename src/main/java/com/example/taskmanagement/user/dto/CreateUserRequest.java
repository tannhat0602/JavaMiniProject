package com.example.taskmanagement.user.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class CreateUserRequest {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    // ===== Constructor =====
    public CreateUserRequest() {
    }

    public CreateUserRequest(String username, String email) {
        this.username = username;
        this.email = email;
    }

    // ===== Getter & Setter =====
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
