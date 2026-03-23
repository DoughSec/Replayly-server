package com.replayly.server.controller;

import com.replayly.server.dto.ApiRequestLogCreateRequest;
import com.replayly.server.dto.ApiRequestLogDetailResponse;
import com.replayly.server.dto.ApiRequestLogSummaryResponse;
import com.replayly.server.dto.ApiRequestLogUpdateRequest;
import com.replayly.server.model.enums.RequestLogStatus;
import com.replayly.server.model.enums.SeverityLevel;
import com.replayly.server.service.ApiRequestLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/request-logs")
public class ApiRequestLogController {

    private final ApiRequestLogService apiRequestLogService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiRequestLogDetailResponse create(@Valid @RequestBody ApiRequestLogCreateRequest request) {
        return apiRequestLogService.create(request);
    }

    @GetMapping
    public List<ApiRequestLogSummaryResponse> findAll(
            @RequestParam(required = false) RequestLogStatus status,
            @RequestParam(required = false) SeverityLevel severity,
            @RequestParam(required = false) String environment,
            @RequestParam(required = false) String method,
            @RequestParam(required = false) String endpoint,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return apiRequestLogService.findAll(status, severity, environment, method, endpoint, tag, startDate, endDate);
    }

    @GetMapping("/{id}")
    public ApiRequestLogDetailResponse findById(@PathVariable Long id) {
        return apiRequestLogService.findById(id);
    }

    @PatchMapping("/{id}")
    public ApiRequestLogDetailResponse update(@PathVariable Long id, @RequestBody ApiRequestLogUpdateRequest request) {
        return apiRequestLogService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        apiRequestLogService.delete(id);
    }

    @PostMapping("/{id}/tags/{tagId}")
    public ApiRequestLogDetailResponse attachTag(@PathVariable Long id, @PathVariable Long tagId) {
        return apiRequestLogService.attachTag(id, tagId);
    }

    @DeleteMapping("/{id}/tags/{tagId}")
    public ApiRequestLogDetailResponse removeTag(@PathVariable Long id, @PathVariable Long tagId) {
        return apiRequestLogService.removeTag(id, tagId);
    }
}
