package com.github.alexishat.backend.web;

import com.github.alexishat.backend.service.JwtService;
import com.github.alexishat.backend.dtos.LoginDto;
import com.github.alexishat.backend.dtos.RegisterDto;
import com.github.alexishat.backend.dtos.UserDto;
import com.github.alexishat.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController()
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService tokenService;

    @PostMapping("/api/auth/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        UserDto userDto = userService.login(loginDto);
        return ResponseEntity.ok(tokenService.generateToken(userDto.getUsername()));
    }

    @PostMapping("/api/auth/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {

            UserDto userDto = userService.register(registerDto);
            String token = tokenService.generateToken(userDto.getUsername());

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(userDto.getId())
                    .toUri();

            return ResponseEntity.created(location).body(token);
    }
}
