package com.github.alexishat.backend.dtos.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = {TimeRangeValidator.class})
public @interface ValidTimeRange {
    String message() default "Startzeit muss vor Endzeit liegen und darf maximal 10 Stunden dauern.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
