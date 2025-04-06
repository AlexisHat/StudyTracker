package com.github.alexishat.backend.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.alexishat.backend.dtos.validation.ValidTimeRange;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ValidTimeRange
public class SessionDto {
    @NotBlank
    private String modus;
    private String newTopic;
    private String topic;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startzeit;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endzeit;
}
