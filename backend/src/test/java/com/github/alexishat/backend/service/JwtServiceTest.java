package com.github.alexishat.backend.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class JwtServiceTest {

    private JwtService jwtService;

    UserService mock;

    @BeforeEach
    void setUp() {
        mock = mock(UserService.class);
        jwtService = new JwtService(mock);

        String jwtSecret = "12345678901234567890123456789012";
        ReflectionTestUtils.setField(jwtService, "jwtSecret", jwtSecret);

        long jwtExpiration = 1000 * 60 * 60;
        ReflectionTestUtils.setField(jwtService, "jwtExpirationTime", jwtExpiration);

        jwtService.init();
    }


    @Test
    @DisplayName("die generate Token gibt ein Token als String zurück")
    public void test_01() {
        String username = "testUser";
        String token = jwtService.generateToken(username);
        assertThat(token.startsWith("eyJhbGciOiJIUzI1NiJ9.")).isTrue(); // Teil des JWT-Headers
    }

    @Test
    @DisplayName("bei einem nicht gültigen Token wird eine Runtime Exception geworfen mit message JWT")
    void test_02() {
        String token = "ungültigerToken";
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> jwtService.validateToken(token));

        assertThat(runtimeException.getMessage()).contains("JWT");
    }

    @Test
    @DisplayName("Bei einem gültigen Token wird der User service aufgerufen")
    void test_03() {
        String username = "testUser";
        String token = jwtService.generateToken(username);
        jwtService.validateToken(token);
        verify(mock).findByLogin(username);
    }
}
