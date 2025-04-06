package com.github.alexishat.backend.dtos.validation;

import com.github.alexishat.backend.dtos.SessionDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Duration;

public class TimeRangeValidator implements ConstraintValidator<ValidTimeRange, SessionDto> {

    @Override
    public boolean isValid(SessionDto session, ConstraintValidatorContext context) {
        if (session == null || session.getStartzeit() == null || session.getEndzeit() == null) {
            return true; // Not Null wird von normalen validator übernommen
        }
        Duration dauer = Duration.between(session.getStartzeit(), session.getEndzeit());
        return (session.getStartzeit().isBefore(session.getEndzeit()) &&
                !dauer.minusHours(10).isPositive());
    }
}