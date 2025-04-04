package com.github.alexishat.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "topic")
@Entity
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
