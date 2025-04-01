package com.github.alexishat.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.alexishat.backend.config.JwtUtil;
import com.github.alexishat.backend.config.SecurityConfig;
import com.github.alexishat.backend.config.UserAuthenticationEntryPoint;
import com.github.alexishat.backend.dtos.LoginDto;
import com.github.alexishat.backend.dtos.UserDto;
import com.github.alexishat.backend.service.UserService;
import com.github.alexishat.backend.web.AuthController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
public class AuthControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtUtil tokenService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserAuthenticationEntryPoint userAuthenticationEntryPoint;


    @Test
    @DisplayName("Wenn sich ein vorhandener User Versucht einzuloggen wird ein ok zurückgegeben")
    void test_01() throws Exception {

        LoginDto login = new LoginDto("wer", "dasliest");
        UserDto userDto = UserDto.builder().username("wer").build();

        when(userService.login(login)).thenReturn(userDto);

        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login))).
                andExpect(status().isOk());
    }
}
