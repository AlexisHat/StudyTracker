package com.github.alexishat.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.alexishat.backend.dtos.SessionDto;
import com.github.alexishat.backend.dtos.UserDto;
import com.github.alexishat.backend.service.JwtService;
import com.github.alexishat.backend.service.SessionService;
import com.github.alexishat.backend.service.TopicService;
import com.github.alexishat.backend.service.UserService;
import com.github.alexishat.backend.web.CreateSessionController;
import com.github.alexishat.backend.web.RestControllerAdvice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CreateSessionControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    UserService userService;

    @MockitoBean
    TopicService topicService;


    @Autowired
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("Ein User welcher kein Jwt Token hat darf nicht auf die resource zugreifen")
    void test_01() throws Exception {
        mvc.perform(get("/api/protected/topics"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Ein User mit gültigem JWT Token kann auf die Url zugreifen und das Set ist leer da kein Topic hinterlegt")
    void test_02() throws Exception {
        when(userService.findByLogin("Caedrel")).thenReturn(UserDto.builder().username("Caedrel").build());

        UserDto user = userService.findByLogin("Caedrel");

        String token = jwtService.generateToken(user.getUsername());

        MvcResult mvcResult = mvc.perform(get("/api/protected/topics")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()).andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("[]");
    }

    @Test
    @DisplayName("Ein User welcher kein Jwt Token hat darf nicht auf die create session zugreifen")
    void test_03() throws Exception {
        mvc.perform(get("/api/protected/create"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Bei Daten mit nur einem new und keinem bestehenden Topic wird created zurück")
    void test_04() throws Exception {
        SessionDto sessionDto = SessionDto.builder()
                .newTopic("Mathe").modus("vorhanden")
                .startzeit(LocalDateTime.now()).endzeit(LocalDateTime.now().plusHours(2))
                .build();

        when(userService.findByLogin("Blub")).thenReturn(UserDto.builder().username("Blub").build());


        UserDto user = userService.findByLogin("Blub");

        String token = jwtService.generateToken(user.getUsername());

        SessionService mock = mock(SessionService.class);
        when(mock.createSessionWithTopicChoice(any(),any())).thenReturn(1);
        CreateSessionController controller = new CreateSessionController(userService,topicService, mock);
        MockMvc build = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(RestControllerAdvice.class)
                .build();

        MvcResult mvcResult = build.perform(post("/api/protected/sessions/create")
                        .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isCreated()).andReturn();
    }

    @Test
    @DisplayName("Bei Daten mit nur einem new und einem bestehenden Topic wird ... zurück")
    void test_05() throws Exception {
        SessionDto sessionDto = SessionDto.builder()
                .topic("Mathe").modus("vorhanden")
                .newTopic("Sexologie")
                .startzeit(LocalDateTime.now()).endzeit(LocalDateTime.now().plusHours(2))
                .build();

        when(userService.findByLogin("Blub")).thenReturn(UserDto.builder().username("Blub").build());

        UserDto user = userService.findByLogin("Blub");

        String token = jwtService.generateToken(user.getUsername());

        MvcResult mvcResult = mvc.perform(post("/api/protected/sessions/create")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().is4xxClientError()).andReturn();
    }

    @Test
    @DisplayName("Bei die endzeit vor der startzeit liegt wird ...")
    void test_06() throws Exception {
        SessionDto sessionDto = SessionDto.builder()
                .topic("Mathe").modus("vorhanden")
                .startzeit(LocalDateTime.now()).endzeit(LocalDateTime.now().minusHours(2))
                .build();

        when(userService.findByLogin("Blub")).thenReturn(UserDto.builder().username("Blub").build());

        UserDto user = userService.findByLogin("Blub");

        String token = jwtService.generateToken(user.getUsername());

        MvcResult mvcResult = mvc.perform(post("/api/protected/sessions/create")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().is4xxClientError()).andReturn();
    }
}

