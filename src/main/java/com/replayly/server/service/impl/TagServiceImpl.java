package com.replayly.server.service.impl;

import com.replayly.server.dto.RequestTagCreateRequest;
import com.replayly.server.dto.TagResponse;
import com.replayly.server.exception.BadRequestException;
import com.replayly.server.model.RequestTag;
import com.replayly.server.repository.RequestTagRepository;
import com.replayly.server.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TagServiceImpl implements TagService {

    private final RequestTagRepository requestTagRepository;

    @Override
    public TagResponse create(RequestTagCreateRequest request) {
        requestTagRepository.findByNameIgnoreCase(request.getName()).ifPresent(existing -> {
            throw new BadRequestException("Tag already exists: " + request.getName());
        });
        RequestTag saved = requestTagRepository.save(RequestTag.builder()
                .name(request.getName())
                .color(request.getColor())
                .description(request.getDescription())
                .build());
        return EntityMapper.toTagResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagResponse> findAll() {
        return requestTagRepository.findAll().stream()
                .map(EntityMapper::toTagResponse)
                .toList();
    }
}
