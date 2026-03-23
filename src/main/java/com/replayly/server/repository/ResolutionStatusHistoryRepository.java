package com.replayly.server.repository;

import com.replayly.server.model.ResolutionStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResolutionStatusHistoryRepository extends JpaRepository<ResolutionStatusHistory, Long> {
    List<ResolutionStatusHistory> findByApiRequestLogIdOrderByChangedAtDesc(Long apiRequestLogId);
}
