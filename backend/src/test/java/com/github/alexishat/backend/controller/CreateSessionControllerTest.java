package com.github.alexishat.backend.controller;

import com.github.alexishat.backend.config.SecurityConfig;
import com.github.alexishat.backend.config.UserAuthenticationEntryPoint;
import com.github.alexishat.backend.dtos.UserDto;
import com.github.alexishat.backend.repositories.TopicRepository;
import com.github.alexishat.backend.repositories.UserRepository;
import com.github.alexishat.backend.service.JwtService;
import com.github.alexishat.backend.service.TopicService;
import com.github.alexishat.backend.service.UserService;
import com.github.alexishat.backend.web.CreateSessionController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CreateSessionControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    UserService userService;

    @Autowired
    private JwtService jwtService;

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
}

