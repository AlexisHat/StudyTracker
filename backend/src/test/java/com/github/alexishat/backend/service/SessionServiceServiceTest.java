package com.github.alexishat.backend.service;

import com.github.alexishat.backend.dtos.SessionDto;
import com.github.alexishat.backend.model.Session;
import com.github.alexishat.backend.model.Topic;
import com.github.alexishat.backend.model.User;
import com.github.alexishat.backend.repositories.SessionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ImportTestcontainers
public class SessionServiceServiceTest{


    @Test
    @DisplayName("create ruft das repo zum speichern aus und gibt eine gespeicherte zurück")
    void test_01(){
        SessionRepository sessionRepository = mock(SessionRepository.class);
        UserService userService= mock(UserService.class);
        TopicService topicService = mock(TopicService.class);

        SessionService service = new SessionService(sessionRepository, userService, topicService);
        Session created = service.create(SessionDto.builder().build(), new User(), new Topic());

        verify(sessionRepository, times(1)).save(any());
    }
}
