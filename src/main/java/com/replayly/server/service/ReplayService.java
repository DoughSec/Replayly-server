package com.replayly.server.service;

import com.replayly.server.dto.ReplayAttemptResponse;
import com.replayly.server.dto.ReplayRequest;
import com.replayly.server.dto.RequestComparisonResponse;

import java.util.List;

public interface ReplayService {
    ReplayAttemptResponse replay(Long apiRequestLogId, ReplayRequest request);
    List<ReplayAttemptResponse> findByRequestLogId(Long apiRequestLogId);
    ReplayAttemptResponse findById(Long replayId);
    RequestComparisonResponse findComparison(Long replayId);
}
