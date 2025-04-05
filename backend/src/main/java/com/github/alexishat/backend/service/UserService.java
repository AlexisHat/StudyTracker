package com.github.alexishat.backend.service;

import com.github.alexishat.backend.dtos.LoginDto;
import com.github.alexishat.backend.dtos.RegisterDto;
import com.github.alexishat.backend.dtos.UserDto;
import com.github.alexishat.backend.exceptions.AppException;
import com.github.alexishat.backend.mapper.UserMapper;
import com.github.alexishat.backend.model.User;
import com.github.alexishat.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    public UserDto findByLogin(String username) {
        User byUsername = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("Unbekannter User", HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(byUsername);
    }

    public User findByUsername(String username) {
        return  userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("Unbekannter User", HttpStatus.NOT_FOUND));
    }

    public UserDto login(LoginDto loginDto){
        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new AppException("Unbekannter User", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            return userMapper.toUserDto(user);
        }else{
            throw new AppException("Ungültiges Password", HttpStatus.UNAUTHORIZED);
        }
    }

    public UserDto register(RegisterDto registerDto) {
        Optional<User> byUsername = userRepository.findByUsername(registerDto.getUsername());

        if (byUsername.isPresent()) {
            throw new AppException("Username existiert bereits", HttpStatus.BAD_REQUEST);
        }
        registerDto.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        User user = userMapper.registerToUser(registerDto);

        User savedUser = userRepository.save(user);

        return userMapper.toUserDto(savedUser);
    }
}
