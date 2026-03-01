package com.example.taskmanagement.user.dto;

public class UserResponse {
    private Long id;
    private String username;
    private String email;

    // ===== Constructor =====

    public UserResponse() {
    }

    public UserResponse(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    // ===== Getter & Setter =====
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }


    public String getEmail() {
        return email;
    }



    
}
