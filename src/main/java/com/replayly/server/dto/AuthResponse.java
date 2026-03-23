package com.replayly.server.dto;

import com.replayly.server.model.enums.UserRole;
import lombok.Builder;

@Builder
public record AuthResponse(
        String tokenType,
        String accessToken,
        long expiresInSeconds,
        String email,
        UserRole role
) {
}
