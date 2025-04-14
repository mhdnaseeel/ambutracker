package com.example.ambutracker.service;

import com.example.ambutracker.dto.LoginDTO;
import com.example.ambutracker.dto.UserRegistrationDTO;
import com.example.ambutracker.entity.User;

import java.util.List;

public interface UserService {
    User registerUser(UserRegistrationDTO registrationDTO);
    User authenticateUser(LoginDTO loginDTO);
    User getUserById(Long id);
    User getUserByUsername(String username);
    User getUserByEmail(String email);
    User getUserByPhoneNumber(String phoneNumber);
    List<User> getAllUsers();
    List<User> getUsersByType(String userType);
    User updateUser(Long id, UserRegistrationDTO userDTO);
    void deleteUser(Long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
} 