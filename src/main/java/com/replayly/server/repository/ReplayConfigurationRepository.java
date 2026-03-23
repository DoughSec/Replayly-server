package com.replayly.server.repository;

import com.replayly.server.model.ReplayConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplayConfigurationRepository extends JpaRepository<ReplayConfiguration, Long> {
    List<ReplayConfiguration> findByApiRequestLogIdOrderByCreatedAtDesc(Long apiRequestLogId);
}
