package com.replayly.server.controller;

import com.replayly.server.dto.IssueNoteRequest;
import com.replayly.server.dto.IssueNoteResponse;
import com.replayly.server.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
public class NoteController {

    private final NoteService noteService;

    @PostMapping("/request-logs/{id}/notes")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('DEVELOPER', 'ADMIN')")
    public IssueNoteResponse add(@PathVariable Long id, @Valid @RequestBody IssueNoteRequest request) {
        return noteService.add(id, request);
    }

    @GetMapping("/request-logs/{id}/notes")
    @PreAuthorize("hasAnyRole('VIEWER', 'DEVELOPER', 'ADMIN')")
    public List<IssueNoteResponse> findByRequestLog(@PathVariable Long id) {
        return noteService.findByRequestLogId(id);
    }

    @PatchMapping("/notes/{noteId}")
    @PreAuthorize("hasAnyRole('DEVELOPER', 'ADMIN')")
    public IssueNoteResponse update(@PathVariable Long noteId, @Valid @RequestBody IssueNoteRequest request) {
        return noteService.update(noteId, request);
    }

    @DeleteMapping("/notes/{noteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('DEVELOPER', 'ADMIN')")
    public void delete(@PathVariable Long noteId) {
        noteService.delete(noteId);
    }
}
