package com.replayly.server.service.impl;

import com.replayly.server.dto.CurrentUserResponse;
import com.replayly.server.exception.ResourceNotFoundException;
import com.replayly.server.model.AppUser;
import com.replayly.server.repository.AppUserRepository;
import com.replayly.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final AppUserRepository appUserRepository;

    @Override
    public CurrentUserResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        AppUser user = appUserRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found: " + email));

        return CurrentUserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
