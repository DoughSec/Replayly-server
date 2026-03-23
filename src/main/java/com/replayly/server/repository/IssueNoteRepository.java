package com.replayly.server.repository;

import com.replayly.server.model.IssueNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueNoteRepository extends JpaRepository<IssueNote, Long> {
    List<IssueNote> findByApiRequestLogIdOrderByCreatedAtDesc(Long apiRequestLogId);
}
