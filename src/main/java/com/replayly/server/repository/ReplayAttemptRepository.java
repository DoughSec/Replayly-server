package com.replayly.server.repository;

import com.replayly.server.model.ReplayAttempt;
import com.replayly.server.repository.projection.ReplayCountView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplayAttemptRepository extends JpaRepository<ReplayAttempt, Long> {

    List<ReplayAttempt> findByApiRequestLogIdOrderByReplayedAtDesc(Long apiRequestLogId);

    @Query("""
            select r.apiRequestLog.id as apiRequestLogId, r.apiRequestLog.requestName as requestName, count(r) as replayCount
            from ReplayAttempt r
            group by r.apiRequestLog.id, r.apiRequestLog.requestName
            order by count(r) desc
            """)
    List<ReplayCountView> findMostReplayed();
}
