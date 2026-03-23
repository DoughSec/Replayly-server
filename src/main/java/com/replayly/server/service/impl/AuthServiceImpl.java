package com.replayly.server.service.impl;

import com.replayly.server.dto.AuthLoginRequest;
import com.replayly.server.dto.AuthRegisterRequest;
import com.replayly.server.dto.AuthResponse;
import com.replayly.server.exception.BadRequestException;
import com.replayly.server.model.AppUser;
import com.replayly.server.model.enums.UserRole;
import com.replayly.server.repository.AppUserRepository;
import com.replayly.server.security.JwtService;
import com.replayly.server.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(AuthLoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        AppUser user = appUserRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));
        return buildAuthResponse(user);
    }

    @Override
    public AuthResponse register(AuthRegisterRequest request) {
        if (appUserRepository.findByEmailIgnoreCase(request.getEmail()).isPresent()) {
            throw new BadRequestException("Email is already in use");
        }

        UserRole role = request.getRole() == null ? UserRole.VIEWER : request.getRole();
        if (role == UserRole.ADMIN) {
            throw new BadRequestException("Admin role cannot be self-assigned");
        }

        AppUser saved = appUserRepository.save(AppUser.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build());

        return buildAuthResponse(saved);
    }

    private AuthResponse buildAuthResponse(AppUser user) {
        return AuthResponse.builder()
                .tokenType("Bearer")
                .accessToken(jwtService.generateToken(user.getEmail(), user.getRole()))
                .expiresInSeconds(jwtService.getExpirationSeconds())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
