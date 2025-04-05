package com.github.alexishat.backend.web;

import com.github.alexishat.backend.dtos.SessionDto;
import com.github.alexishat.backend.dtos.UserDto;
import com.github.alexishat.backend.model.Session;
import com.github.alexishat.backend.model.Topic;
import com.github.alexishat.backend.model.User;
import com.github.alexishat.backend.service.SessionService;
import com.github.alexishat.backend.service.TopicService;
import com.github.alexishat.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
                                                 @RequestBody SessionDto session) {

        if ((session.getNewTopic() == null) == (session.getTopic() == null)) {
            return ResponseEntity.badRequest().build(); // Entweder oder, nicht beides oder keins
        }

        User user = userService.findByUsername(userDto.getUsername());
        Topic topic;

        if (session.getNewTopic() != null) {
            topic = topicService.createNewTopicForUser(session.getNewTopic(), user);
        } else {
            topic = topicService.findByNameAndUser(session.getTopic(), user);
        }

        Session createdSession = sessionService.create(session, user, topic);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdSession.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdSession.getId());
    }
}
