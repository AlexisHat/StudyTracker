package com.github.alexishat.backend.service;

import com.github.alexishat.backend.dtos.SessionDto;
import com.github.alexishat.backend.model.Session;
import com.github.alexishat.backend.model.Topic;
import com.github.alexishat.backend.model.User;
import com.github.alexishat.backend.repositories.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    public Session create(SessionDto sessionDto, User user, Topic topic) {
        Session session = new Session();
        session.setTopic(topic);
        session.setUser(user);
        session.setStartzeit(sessionDto.getStartzeit());
        session.setEndzeit(sessionDto.getEndzeit());
        return sessionRepository.save(session);
    }

}
