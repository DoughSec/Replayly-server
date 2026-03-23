package com.replayly.server.controller;

import com.replayly.server.dto.ReplayAttemptResponse;
import com.replayly.server.dto.ReplayRequest;
import com.replayly.server.dto.RequestComparisonResponse;
import com.replayly.server.service.ReplayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReplayController {

    private final ReplayService replayService;

    @PostMapping("/request-logs/{id}/replay")
    @ResponseStatus(HttpStatus.CREATED)
    public ReplayAttemptResponse replay(@PathVariable Long id, @RequestBody(required = false) ReplayRequest request) {
        ReplayRequest safeRequest = request == null ? new ReplayRequest() : request;
        return replayService.replay(id, safeRequest);
    }

    @GetMapping("/request-logs/{id}/replays")
    public List<ReplayAttemptResponse> findByRequestLog(@PathVariable Long id) {
        return replayService.findByRequestLogId(id);
    }

    @GetMapping("/replays/{replayId}")
    public ReplayAttemptResponse findReplay(@PathVariable Long replayId) {
        return replayService.findById(replayId);
    }

    @GetMapping("/replays/{replayId}/comparison")
    public RequestComparisonResponse findComparison(@PathVariable Long replayId) {
        return replayService.findComparison(replayId);
    }
}
