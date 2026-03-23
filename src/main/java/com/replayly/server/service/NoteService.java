package com.replayly.server.service;

import com.replayly.server.dto.IssueNoteRequest;
import com.replayly.server.dto.IssueNoteResponse;

import java.util.List;

public interface NoteService {
    IssueNoteResponse add(Long apiRequestLogId, IssueNoteRequest request);
    List<IssueNoteResponse> findByRequestLogId(Long apiRequestLogId);
    IssueNoteResponse update(Long noteId, IssueNoteRequest request);
    void delete(Long noteId);
}
