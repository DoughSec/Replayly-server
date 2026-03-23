package com.replayly.server.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class IssueNoteRequest {
    @NotBlank
    private String noteText;
    private String createdBy;
}
