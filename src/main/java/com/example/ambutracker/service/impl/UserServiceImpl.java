package com.example.ambutracker.service.impl;

import com.example.ambutracker.dto.LoginDTO;
import com.example.ambutracker.dto.UserRegistrationDTO;
import com.example.ambutracker.entity.User;
import com.example.ambutracker.entity.UserType;
import com.example.ambutracker.repository.UserRepository;
import com.example.ambutracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public User registerUser(UserRegistrationDTO registrationDTO) {
        // Check if username, email, or phone number already exists
        if (existsByUsername(registrationDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (existsByEmail(registrationDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (existsByPhoneNumber(registrationDTO.getPhoneNumber())) {
            throw new RuntimeException("Phone number already exists");
        }

        // Create a new user
        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setEmail(registrationDTO.getEmail());
        user.setFullName(registrationDTO.getFullName());
        user.setPhoneNumber(registrationDTO.getPhoneNumber());
        user.setUserType(UserType.MEDICAL_STAFF);

        return userRepository.save(user);
    }

    @Override
    public User authenticateUser(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        return getUserByUsername(loginDTO.getUsername());
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    @Override
    public User getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("User not found with phone number: " + phoneNumber));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getUsersByType(String userType) {
        try {
            UserType type = UserType.valueOf(userType.toUpperCase());
            return userRepository.findByUserType(type);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid user type: " + userType);
        }
    }

    @Override
    public User updateUser(Long id, UserRegistrationDTO userDTO) {
        User existingUser = getUserById(id);
        
        // Check if the new username, email, or phone number already exists for another user
        if (!existingUser.getUsername().equals(userDTO.getUsername()) && existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (!existingUser.getEmail().equals(userDTO.getEmail()) && existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (!existingUser.getPhoneNumber().equals(userDTO.getPhoneNumber()) && existsByPhoneNumber(userDTO.getPhoneNumber())) {
            throw new RuntimeException("Phone number already exists");
        }
        
        existingUser.setUsername(userDTO.getUsername());
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setFullName(userDTO.getFullName());
        existingUser.setPhoneNumber(userDTO.getPhoneNumber());
        if (userDTO.getUserType() != null) {
            existingUser.setUserType(userDTO.getUserType());
        }
        
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }
} 