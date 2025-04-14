package com.github.alexishat.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionDayDto {
    private String topic;
    private int duration;
    private String description;
    LocalDateTime startTime;
    LocalDateTime endTime;
}
