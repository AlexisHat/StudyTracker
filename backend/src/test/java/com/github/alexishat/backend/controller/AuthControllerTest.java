package com.github.alexishat.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.alexishat.backend.config.JwtUtil;
import com.github.alexishat.backend.config.SecurityConfig;
import com.github.alexishat.backend.config.UserAuthenticationEntryPoint;
import com.github.alexishat.backend.dtos.LoginDto;
import com.github.alexishat.backend.dtos.RegisterDto;
import com.github.alexishat.backend.dtos.UserDto;
import com.github.alexishat.backend.exceptions.AppException;
import com.github.alexishat.backend.service.UserService;
import com.github.alexishat.backend.web.AuthController;
import com.github.alexishat.backend.web.RestControllerAdvice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({AuthController.class, RestControllerAdvice.class})
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

    @Test
    @DisplayName("Wenn sich ein User versucht einzuloggen welcher kein Konto hat wird eine App Exception mit NOT_FOUND")
    void test_02() throws Exception {

        LoginDto login = new LoginDto("wer", "dasliest");

        UserService mockService = mock(UserService.class);
        when(mockService.login(login)).thenThrow(new AppException("Unbekannter User", HttpStatus.NOT_FOUND));

        AuthController controller = new AuthController(mockService, mock(JwtUtil.class));

        MockMvc build = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(RestControllerAdvice.class)
                .build();

        MvcResult mvcResult = build.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isNotFound()).andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOf(AppException.class);
    }
    @Test
    @DisplayName("Bei gültigem Login wird ein Token, welches vom tokenService erstellt wurde zurückgegeben")
    void test_03() throws Exception {

        LoginDto loginDto = new LoginDto("wer", "dasliest");
        UserDto userDto = UserDto.builder().username("wer").build();
        String expectedToken = "jwttoken123";

        when(userService.login(loginDto)).thenReturn(userDto);
        when(tokenService.generateToken("wer")).thenReturn(expectedToken);

        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedToken));
    }

    @Test
    @DisplayName("wenn ein Benutzer mit daten die noch nicht belegt sind wird created zurück mit token im body und URI")
    void test_04() throws Exception {

        RegisterDto registerDto = RegisterDto.builder().username("wer").password("dasliest").email("ist@mail.com").fullName("doof").build();
        UserDto user = UserDto.builder().username("wer").email("ist@mail.com").fullName("doof").id(69L).build();
        when(userService.register(registerDto)).thenReturn(user);

        mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/auth/register/69"));
    }
}
