package com.replayly.server.repository;

import com.replayly.server.model.ApiRequestLog;
import com.replayly.server.model.enums.RequestLogStatus;
import com.replayly.server.model.enums.SeverityLevel;
import com.replayly.server.repository.projection.LabelCountView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApiRequestLogRepository extends JpaRepository<ApiRequestLog, Long>, JpaSpecificationExecutor<ApiRequestLog> {

    long countByStatus(RequestLogStatus status);

    long countBySeverity(SeverityLevel severity);

    @Query("""
            select cast(l.status as string) as label, count(l) as total
            from ApiRequestLog l
            group by l.status
            """)
    List<LabelCountView> countGroupedByStatus();

    @Query("""
            select cast(l.severity as string) as label, count(l) as total
            from ApiRequestLog l
            group by l.severity
            """)
    List<LabelCountView> countGroupedBySeverity();

    @Query("""
            select coalesce(l.endpointPath, 'UNKNOWN') as label, count(l) as total
            from ApiRequestLog l
            group by l.endpointPath
            order by count(l) desc
            """)
    List<LabelCountView> countGroupedByEndpoint();

    @Query("""
            select coalesce(cast(l.responseStatus as string), 'UNKNOWN') as label, count(l) as total
            from ApiRequestLog l
            group by l.responseStatus
            order by count(l) desc
            """)
    List<LabelCountView> countGroupedByResponseStatus();
}
