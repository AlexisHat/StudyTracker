package com.github.alexishat.backend.service;

import com.github.alexishat.backend.model.Topic;
import com.github.alexishat.backend.repositories.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;

    public Set<String> findAllTopicNamesForUser(Long userId) {
        return topicRepository.findByUserId(userId).stream()
                .map(Topic::getName)
                .collect(Collectors.toSet());
    }
}
