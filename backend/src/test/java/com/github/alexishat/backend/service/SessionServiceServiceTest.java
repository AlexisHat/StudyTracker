package com.github.alexishat.backend.service;

import com.github.alexishat.backend.dtos.SessionDto;
import com.github.alexishat.backend.model.Session;
import com.github.alexishat.backend.model.Topic;
import com.github.alexishat.backend.model.User;
import com.github.alexishat.backend.repositories.SessionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


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

    @Test
    @DisplayName("get Minutes for every day gibt eine Map zurück die überall 0 minuten hat außer wo eine Session vorhanden")
    void test_02(){
        SessionRepository sessionRepository = mock(SessionRepository.class);
        UserService userService= mock(UserService.class);
        TopicService topicService = mock(TopicService.class);
        SessionService service = new SessionService(sessionRepository, userService, topicService);
        Session session = Session.builder().startzeit(LocalDateTime.now()).endzeit(LocalDateTime.now().plusHours(2)).build();
        User user = new User();
        when(userService.findByUsername("bla")).thenReturn(user);
        when(sessionRepository.findAllByUserAndYear(user,2025)).thenReturn(List.of(session));

        Map<String, Integer> bla = service.getMinutesForEveryDayInTheYear("bla", 2025);

        Integer in3Tagen = bla.get(LocalDate.now().plusDays(3).toString());
        assertThat(in3Tagen).isEqualTo(0);
        assertThat(bla.get(LocalDate.now().toString())).isGreaterThan(1);
    }

    @Test
    @DisplayName("Die Map ist am ende sorted aufsteigend nach den Daten")
    void test_03(){
        SessionRepository sessionRepository = mock(SessionRepository.class);
        UserService userService= mock(UserService.class);
        TopicService topicService = mock(TopicService.class);
        SessionService service = new SessionService(sessionRepository, userService, topicService);
        User user = new User();
        when(userService.findByUsername("bla")).thenReturn(user);

        Map<String, Integer> bla = service.getMinutesForEveryDayInTheYear("bla", 2025);

        List<String> dateList = bla.keySet().stream().toList();

        assertThat(dateList).isEqualTo(dateList.stream().sorted().toList());
    }

    @Test
    @DisplayName("Die Map ist am ende sorted aufsteigend nach den Daten also erste key ist 1. jan")
    void test_04(){
        SessionRepository sessionRepository = mock(SessionRepository.class);
        UserService userService= mock(UserService.class);
        TopicService topicService = mock(TopicService.class);
        SessionService service = new SessionService(sessionRepository, userService, topicService);
        User user = new User();
        when(userService.findByUsername("bla")).thenReturn(user);

        Map<String, Integer> bla = service.getMinutesForEveryDayInTheYear("bla", 2025);

        List<String> list = bla.keySet().stream().toList();

        assertThat(list.getFirst()).isEqualTo(LocalDate.of(2025,1,1).toString());
    }
}