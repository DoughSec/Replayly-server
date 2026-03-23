package com.replayly.server.repository;

import com.replayly.server.model.RequestTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequestTagRepository extends JpaRepository<RequestTag, Long> {
    Optional<RequestTag> findByNameIgnoreCase(String name);
}
