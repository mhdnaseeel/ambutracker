package com.example.ambutracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "AmbuTracker API",
        version = "1.0",
        description = "REST API documentation for AmbuTracker application"
    ),
    servers = {
        @Server(
            url = "http://localhost:8080",
            description = "Local Development Server"
        )
    }
)
public class AmbuTrackerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AmbuTrackerApplication.class, args);
    }
} 