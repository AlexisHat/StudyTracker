package com.github.alexishat.backend.web;

import com.github.alexishat.backend.dtos.SessionDto;
import com.github.alexishat.backend.dtos.UserDto;
import com.github.alexishat.backend.exceptions.SessionValidException;
import com.github.alexishat.backend.model.Session;
import com.github.alexishat.backend.model.Topic;
import com.github.alexishat.backend.model.User;
import com.github.alexishat.backend.service.SessionService;
import com.github.alexishat.backend.service.TopicService;
import com.github.alexishat.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Set;

@RestController()
@RequestMapping("/api/protected")
@RequiredArgsConstructor
public class CreateSessionController {

    private final UserService userService;
    private final TopicService topicService;
    private final SessionService sessionService;

    @GetMapping("/topics")
    public ResponseEntity<Set<String>> getTopicsForUser(@AuthenticationPrincipal UserDto userDto){
        Long id = userDto.getId();
        return ResponseEntity.ok(topicService.findAllTopicNamesForUser(id));
    }

    @PostMapping("/sessions/create")
    public ResponseEntity<Integer> createSession(@AuthenticationPrincipal UserDto userDto,
                                                 @Valid @RequestBody SessionDto session,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new SessionValidException(bindingResult);
        }

        int sessionId = sessionService.createSessionWithTopicChoice(session, userDto.getUsername());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(sessionId)
                .toUri();

        return ResponseEntity.created(location).body(sessionId);
    }
}
