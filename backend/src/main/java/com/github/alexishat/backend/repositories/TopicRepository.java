package com.github.alexishat.backend.repositories;

import com.github.alexishat.backend.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {
    Set<Topic> findByUserId(Long userId);
}
