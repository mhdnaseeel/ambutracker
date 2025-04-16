package com.example.ambutracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmergencyButtonController {
    
    @GetMapping("/emergency-button")
    public String showEmergencyButton() {
        return "emergency-button";
    }
} 