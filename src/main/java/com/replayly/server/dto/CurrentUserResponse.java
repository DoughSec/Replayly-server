package com.replayly.server.dto;

import com.replayly.server.model.enums.UserRole;
import lombok.Builder;

@Builder
public record CurrentUserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        UserRole role
) {
}
