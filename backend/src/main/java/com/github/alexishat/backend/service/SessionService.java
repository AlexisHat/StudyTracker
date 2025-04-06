package com.github.alexishat.backend.service;

import com.github.alexishat.backend.dtos.SessionDto;
import com.github.alexishat.backend.exceptions.SessionModusException;
import com.github.alexishat.backend.model.Session;
import com.github.alexishat.backend.model.Topic;
import com.github.alexishat.backend.model.User;
import com.github.alexishat.backend.repositories.SessionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserService userService;
    private final TopicService topicService;

    public Session create(SessionDto sessionDto, User user, Topic topic) {
        Session session = new Session();
        session.setTopic(topic);
        session.setUser(user);
        session.setStartzeit(sessionDto.getStartzeit());
        session.setEndzeit(sessionDto.getEndzeit());
        return sessionRepository.save(session);
    }

    public int createSessionWithTopicChoice(@Valid SessionDto session, String username) {
            if ((session.getNewTopic() == null) == (session.getTopic() == null)) {
                throw new SessionModusException("Entweder ein bestehendes oder ein neues Thema angeben.");
            }

            User user = userService.findByUsername(username);
            Topic topic;

            if (session.getNewTopic() != null) {
                topic = topicService.createNewTopicForUser(session.getNewTopic(), user);
            } else {
                topic = topicService.findByNameAndUser(session.getTopic(), user);
            }

            Session createdSession = create(session, user, topic);

            return createdSession.getId();
        }

    }
