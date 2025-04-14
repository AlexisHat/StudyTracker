package com.github.alexishat.backend.service;

import com.github.alexishat.backend.dtos.SessionDayDto;
import com.github.alexishat.backend.dtos.SessionDto;
import com.github.alexishat.backend.exceptions.SessionModusException;
import com.github.alexishat.backend.model.Session;
import com.github.alexishat.backend.model.Topic;
import com.github.alexishat.backend.model.User;
import com.github.alexishat.backend.repositories.SessionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

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
        session.setDescription(sessionDto.getDescription());
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

    public Map<String, Integer> getMinutesForEveryDayInTheYear(String username, int year) {
        User byUsername = userService.findByUsername(username);
        List<Session> allByUserAndYear = sessionRepository.findAllByUserAndYear(byUsername, year);

        Map<String, Integer> minutesPerDay = allByUserAndYear.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getEndzeit().toLocalDate().toString(),
                        TreeMap::new,
                        Collectors.summingInt(s -> (int) Duration.between(s.getStartzeit(), s.getEndzeit()).toMinutes())
                ));

        return fillMissingDaysWithZero(year, minutesPerDay);
    }

    private Map<String, Integer> fillMissingDaysWithZero(int year, Map<String, Integer> minutesPerDay) {
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);

        Map<String, Integer> completeMap = new LinkedHashMap<>();

        while (!start.isAfter(end)) {
            String dateKey = start.toString();
            completeMap.put(dateKey, minutesPerDay.getOrDefault(dateKey, 0));
            start = start.plusDays(1);
        }

        return completeMap;
    }

    public List<SessionDayDto> getSessionInfoForDay(LocalDate day, String username) {
        User user = userService.findByUsername(username);
        LocalDateTime startOfDay = day.atStartOfDay();
        LocalDateTime endOfDay = day.plusDays(1).atStartOfDay();
        List<Session> allByUserAndDay = sessionRepository.findAllByUserAndDay(user, startOfDay, endOfDay);
        List<SessionDayDto> sessionDayDtos = new ArrayList<>();
        for (Session session : allByUserAndDay) {
            SessionDayDto sessionDayDto = SessionDayDto.builder()
                    .description(session.getDescription())
                    .startTime(session.getStartzeit())
                    .endTime(session.getEndzeit())
                    .topic(session.getTopic().toString())
                    .duration((int) session.getStartzeit().until(session.getEndzeit(), ChronoUnit.MINUTES))
                    .build();
            sessionDayDtos.add(sessionDayDto);
        }
        return sessionDayDtos;
    }
}

