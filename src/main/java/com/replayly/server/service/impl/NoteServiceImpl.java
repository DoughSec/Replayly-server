package com.replayly.server.service.impl;

import com.replayly.server.dto.IssueNoteRequest;
import com.replayly.server.dto.IssueNoteResponse;
import com.replayly.server.exception.ResourceNotFoundException;
import com.replayly.server.model.ApiRequestLog;
import com.replayly.server.model.IssueNote;
import com.replayly.server.repository.ApiRequestLogRepository;
import com.replayly.server.repository.IssueNoteRepository;
import com.replayly.server.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NoteServiceImpl implements NoteService {

    private final IssueNoteRepository issueNoteRepository;
    private final ApiRequestLogRepository apiRequestLogRepository;

    @Override
    public IssueNoteResponse add(Long apiRequestLogId, IssueNoteRequest request) {
        ApiRequestLog log = apiRequestLogRepository.findById(apiRequestLogId)
                .orElseThrow(() -> new ResourceNotFoundException("Request log not found: " + apiRequestLogId));
        IssueNote note = IssueNote.builder()
                .apiRequestLog(log)
                .noteText(request.getNoteText())
                .createdBy(request.getCreatedBy())
                .build();
        return EntityMapper.toNoteResponse(issueNoteRepository.save(note));
    }

    @Override
    @Transactional(readOnly = true)
    public List<IssueNoteResponse> findByRequestLogId(Long apiRequestLogId) {
        return issueNoteRepository.findByApiRequestLogIdOrderByCreatedAtDesc(apiRequestLogId).stream()
                .map(EntityMapper::toNoteResponse)
                .toList();
    }

    @Override
    public IssueNoteResponse update(Long noteId, IssueNoteRequest request) {
        IssueNote note = issueNoteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found: " + noteId));
        note.setNoteText(request.getNoteText());
        if (request.getCreatedBy() != null) {
            note.setCreatedBy(request.getCreatedBy());
        }
        return EntityMapper.toNoteResponse(issueNoteRepository.save(note));
    }

    @Override
    public void delete(Long noteId) {
        IssueNote note = issueNoteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found: " + noteId));
        issueNoteRepository.delete(note);
    }
}
