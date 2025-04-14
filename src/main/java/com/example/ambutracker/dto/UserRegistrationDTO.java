package com.example.ambutracker.dto;

import com.example.ambutracker.entity.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDTO {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String phoneNumber;
    private UserType userType;
} 