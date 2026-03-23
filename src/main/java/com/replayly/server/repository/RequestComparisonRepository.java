package com.replayly.server.repository;

import com.replayly.server.model.RequestComparison;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequestComparisonRepository extends JpaRepository<RequestComparison, Long> {
    Optional<RequestComparison> findByReplayAttemptId(Long replayAttemptId);
}
