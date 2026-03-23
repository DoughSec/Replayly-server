package com.replayly.server.config;

import com.replayly.server.model.AppUser;
import com.replayly.server.model.enums.UserRole;
import com.replayly.server.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class SecurityDataInitializer {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner seedUsers() {
        return args -> {
            seedUser("Admin", "User", "admin@replayly.local", "admin123!", UserRole.ADMIN);
            seedUser("Dev", "User", "developer@replayly.local", "developer123!", UserRole.DEVELOPER);
            seedUser("View", "User", "viewer@replayly.local", "viewer123!", UserRole.VIEWER);
        };
    }

    private void seedUser(String firstName, String lastName, String email, String rawPassword, UserRole role) {
        if (appUserRepository.findByEmailIgnoreCase(email).isPresent()) {
            return;
        }
        appUserRepository.save(AppUser.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .passwordHash(passwordEncoder.encode(rawPassword))
                .role(role)
                .build());
    }
}
