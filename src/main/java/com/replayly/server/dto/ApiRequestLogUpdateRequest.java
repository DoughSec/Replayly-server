package com.replayly.server.dto;

import com.replayly.server.model.enums.RequestLogStatus;
import com.replayly.server.model.enums.SeverityLevel;
import lombok.Data;

import java.util.Set;

@Data
public class ApiRequestLogUpdateRequest {
    private String requestName;
    private RequestLogStatus status;
    private SeverityLevel severity;
    private Set<Long> tagIds;
    private String changedBy;
    private String reason;
}
