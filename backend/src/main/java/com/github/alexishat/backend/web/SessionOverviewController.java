package com.github.alexishat.backend.web;

import com.github.alexishat.backend.dtos.UserDto;
import com.github.alexishat.backend.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/protected")
@RequiredArgsConstructor
public class SessionOverviewController {

    private final SessionService sessionService;

    @GetMapping("/sessions/year")
    public ResponseEntity<Map<String, Integer>> sessionsForYear(@AuthenticationPrincipal UserDto user, @RequestParam int year){
        return ResponseEntity.ok(sessionService.getMinutesForEveryDayInTheYear(user.getUsername(), year));
    }
}
