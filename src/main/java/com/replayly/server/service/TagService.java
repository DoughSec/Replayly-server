package com.replayly.server.service;

import com.replayly.server.dto.RequestTagCreateRequest;
import com.replayly.server.dto.TagResponse;

import java.util.List;

public interface TagService {
    TagResponse create(RequestTagCreateRequest request);
    List<TagResponse> findAll();
}
