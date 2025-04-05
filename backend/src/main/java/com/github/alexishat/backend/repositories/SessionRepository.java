package com.github.alexishat.backend.repositories;

import com.github.alexishat.backend.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Integer> {
}
