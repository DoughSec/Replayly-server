package com.replayly.server.dto;

import lombok.Builder;

@Builder
public record LabelCountResponse(
        String label,
        long total
) {
}
