package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;


public class DateAfterValidator implements ConstraintValidator<DateAfter, LocalDate> {

    private static final LocalDate MIN_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public void initialize(DateAfter constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return !value.isBefore(MIN_DATE);
    }
}
