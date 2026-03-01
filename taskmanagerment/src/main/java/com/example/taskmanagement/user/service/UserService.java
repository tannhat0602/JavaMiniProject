package com.example.taskmanagement.user.service;

import java.util.List;

import com.example.taskmanagement.user.entity.User;
import com.example.taskmanagement.user.repository.UserRepository;
import com.example.taskmanagement.exception.ResourceNotFoundException;
import com.example.taskmanagement.exception.DuplicateResourceException;
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;

    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + id));
    }

    public User createUser(User user) {

        // Check username trùng
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateResourceException(
                    "Username already exists: " + user.getUsername());
        }

        return userRepository.save(user);
    }
    public User updateUser(Long id, User updatedUser) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + id));

        // Cập nhật dữ liệu
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());

        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + id));

        userRepository.delete(existingUser);
    }
}
