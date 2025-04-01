package com.github.alexishat.backend;

import com.github.alexishat.backend.containercofig.TestContainerConfiguration;
import org.springframework.boot.SpringApplication;

public class BackendApplicationWithContainer {
    public static void main(String[] args) {
        SpringApplication.from(BackendApplication::main)
                .with(TestContainerConfiguration.class)
                .run(args);
    }
}
