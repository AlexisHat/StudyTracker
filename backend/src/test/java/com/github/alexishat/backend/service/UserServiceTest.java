package com.github.alexishat.backend.service;


import com.github.alexishat.backend.dtos.LoginDto;
import com.github.alexishat.backend.dtos.RegisterDto;
import com.github.alexishat.backend.exceptions.AppException;
import com.github.alexishat.backend.mapper.UserMapper;
import com.github.alexishat.backend.model.User;
import com.github.alexishat.backend.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Test
    @DisplayName("Die methode login ruft bei richtigen Password die Mapper Methode auf")
    void test_01() {
        UserRepository userRepository = mock(UserRepository.class);
        UserMapper userMapper = mock(UserMapper.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        LoginDto login = LoginDto.builder().username("hallo").password("ciao").build();
        User user = new User();
        user.setUsername("hallo");
        user.setPassword(passwordEncoder.encode("ciao"));

        when(userRepository.findByUsername(login.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(login.getPassword(), user.getPassword())).thenReturn(true);

        UserService userService = new UserService(userRepository, userMapper, passwordEncoder);

        userService.login(login);

        verify(userMapper).toUserDto(user);
    }

    @Test
    @DisplayName("login wirft bei falschem passwort eine exception")
    void test_02() {
        UserRepository userRepository = mock(UserRepository.class);
        UserMapper userMapper = mock(UserMapper.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        LoginDto login = LoginDto.builder().username("hallo").password("ciao").build();
        User user = new User();
        user.setUsername("hallo");
        user.setPassword(passwordEncoder.encode("ciao"));

        when(userRepository.findByUsername(login.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(login.getPassword(), user.getPassword())).thenReturn(false);

        UserService userService = new UserService(userRepository, userMapper, passwordEncoder);

        AppException exception = assertThrows(AppException.class, () -> {
            userService.login(login);
        });

        assertThat(exception.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("Wenn ein noch nicht vorhandenner User sich versucht zu regestrieren wird ein neuer User in der Datenbank angelegt")
    void test_03() {
        UserRepository userRepository = mock(UserRepository.class);
        UserMapper userMapper = mock(UserMapper.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        RegisterDto registerDto = RegisterDto.builder().username("hallo").password("ciao").build();

        when(userRepository.findByUsername(registerDto.getUsername())).thenReturn(Optional.empty());

        UserService userService = new UserService(userRepository, userMapper, passwordEncoder);

        userService.register(registerDto);

        verify(userRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Wenn ein User schon vorhanden ist wird eine Exception geworfen mit Fehlercode")
    void test_04() {
        UserRepository userRepository = mock(UserRepository.class);
        UserMapper userMapper = mock(UserMapper.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        RegisterDto registerDto = RegisterDto.builder().username("hallo").password("ciao").build();

        when(userRepository.findByUsername(registerDto.getUsername())).thenReturn(Optional.of(new User()));

        UserService userService = new UserService(userRepository, userMapper, passwordEncoder);

        AppException ex = assertThrows(AppException.class , ()->userService.register(registerDto));

        assertThat(ex.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("die find by login methode mappt auf dto wenn ein user gefunden wird")
    void test_05() {
        UserRepository userRepository = mock(UserRepository.class);
        UserMapper userMapper = mock(UserMapper.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        LoginDto login = LoginDto.builder().username("hallo").password("ciao").build();

        when(userRepository.findByUsername(login.getUsername())).thenReturn(Optional.of(new User()));
        UserService userService = new UserService(userRepository, userMapper, passwordEncoder);

        userService.findByLogin(login.getUsername());
        verify(userMapper, times(1)).toUserDto(any());
    }

    @Test
    @DisplayName("die find by login methode wirft exception wenn kein user gefunden wird")
    void test_06() {
        UserRepository userRepository = mock(UserRepository.class);
        UserMapper userMapper = mock(UserMapper.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        LoginDto login = LoginDto.builder().username("hallo").password("ciao").build();

        when(userRepository.findByUsername(login.getUsername())).thenReturn(Optional.empty());
        UserService userService = new UserService(userRepository, userMapper, passwordEncoder);

        AppException ex = assertThrows(AppException.class , () -> userService.findByLogin(login.getUsername()));

        assertThat(ex.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
