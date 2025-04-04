package com.github.alexishat.backend.web;

import com.github.alexishat.backend.dtos.UserDto;
import com.github.alexishat.backend.model.Topic;
import com.github.alexishat.backend.service.TopicService;
import com.github.alexishat.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController()
@RequestMapping("/api/protected")
@RequiredArgsConstructor
public class CreateSessionController {

    private final UserService userService;
    private final TopicService topicService;

    @GetMapping("/topics")
    public ResponseEntity<Set<String>> getTopicsForUser(@AuthenticationPrincipal UserDto userDto){
        Long id = userDto.getId();
        return ResponseEntity.ok(topicService.findAllTopicNamesForUser(id));
    }
}
