package com.replayly.server.dto;

import lombok.Builder;

@Builder
public record TagResponse(
        Long id,
        String name,
        String color,
        String description
) {
}
