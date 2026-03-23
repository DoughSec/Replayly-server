package com.replayly.server.dto;

import com.replayly.server.model.enums.RequestLogStatus;
import com.replayly.server.model.enums.SeverityLevel;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ApiRequestLogCreateRequest {
    @NotBlank
    private String requestName;
    @NotBlank
    private String method;
    private String baseUrl;
    private String endpointPath;
    private String fullUrl;
    private String requestHeaders;
    private String queryParams;
    private String pathParams;
    private String requestBody;
    private String contentType;
    private Integer responseStatus;
    private String responseHeaders;
    private String responseBody;
    private String errorMessage;
    private String sourceApplication;
    @NotBlank
    private String environment;
    private LocalDateTime capturedAt;
    private String createdBy;
    private RequestLogStatus status;
    private SeverityLevel severity;
    private Set<Long> tagIds;
}
