package com.github.alexishat.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Session{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Topic topic;

    @Column(nullable = false)
    private LocalDateTime startzeit;

    @Column(nullable = false)
    private LocalDateTime endzeit;

    @ManyToOne(optional = false)
    private User user;
}