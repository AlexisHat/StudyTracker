package com.github.alexishat.backend.service;

import com.github.alexishat.backend.dtos.SessionDayDto;
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

        LocalDate fixedDate = LocalDate.of(2025, 3, 4);
        LocalDateTime fixedStart = fixedDate.atTime(12, 0);
        LocalDateTime fixedEnd = fixedStart.plusHours(2);

        Session session = Session.builder().startzeit(fixedStart).endzeit(fixedEnd).build();
        User user = new User();
        when(userService.findByUsername("bla")).thenReturn(user);
        when(sessionRepository.findAllByUserAndYear(user,2025)).thenReturn(List.of(session));

        Map<String, Integer> bla = service.getMinutesForEveryDayInTheYear("bla", 2025);


        assertThat(bla.get("2025-01-01")).isEqualTo(0);
        assertThat(bla.get(fixedDate.toString())).isGreaterThan(1);
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

    @Test
    @DisplayName("die DTos werden korrekt geamappt")
    void test_05(){
        SessionRepository sessionRepository = mock(SessionRepository.class);
        UserService userService= mock(UserService.class);
        TopicService topicService = mock(TopicService.class);
        SessionService service = new SessionService(sessionRepository, userService, topicService);
        User user = new User();
        Session session = Session.builder()
                .id(1)
                .topic(Topic.builder()
                        .id(1)
                        .name("Mathe")
                        .user(user)
                        .build())
                .startzeit(LocalDateTime.of(2025, 4, 14, 10, 0))
                .endzeit(LocalDateTime.of(2025, 4, 14, 11, 30))
                .description("Klausurvorbereitung Analysis")
                .user(user)
                .build();
        Session session2 = Session.builder()
                .id(2)
                .topic(Topic.builder()
                        .id(1)
                        .name("Mathe")
                        .user(user)
                        .build())
                .startzeit(LocalDateTime.of(2025, 4, 14, 13, 0))
                .endzeit(LocalDateTime.of(2025, 4, 14, 14, 30))
                .description("Klausurvorbereitung Analysis")
                .user(user)
                .build();
        when(userService.findByUsername("bla")).thenReturn(user);
        when(sessionRepository.findAllByUserAndDay(eq(user),any(),any())).thenReturn(List.of(session,session2));

        List<SessionDayDto> sessionInfoForDay = service.getSessionInfoForDay(LocalDate.now(), "bla");

        assertThat(sessionInfoForDay.size()).isEqualTo(2);
        assertThat(sessionInfoForDay.getFirst().getStartTime()).isEqualTo(session.getStartzeit());
    }

}