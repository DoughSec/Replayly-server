package com.replayly.server.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestTagCreateRequest {
    @NotBlank
    private String name;
    private String color;
    private String description;
}
