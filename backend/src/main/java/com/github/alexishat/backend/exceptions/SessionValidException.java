package com.github.alexishat.backend.exceptions;

import lombok.Getter;
import org.springframework.validation.BindingResult;

public class SessionValidException extends RuntimeException {

    @Getter
    private final BindingResult bindingResult;

    public SessionValidException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

}
