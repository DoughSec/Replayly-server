package com.replayly.server.controller;

import com.replayly.server.dto.RequestTagCreateRequest;
import com.replayly.server.dto.TagResponse;
import com.replayly.server.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagResponse create(@Valid @RequestBody RequestTagCreateRequest request) {
        return tagService.create(request);
    }

    @GetMapping
    public List<TagResponse> findAll() {
        return tagService.findAll();
    }
}
