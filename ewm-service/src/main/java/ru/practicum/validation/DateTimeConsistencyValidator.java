package ru.practicum.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import java.time.LocalDateTime;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class DateTimeConsistencyValidator implements ConstraintValidator<DateTimeConsistency, Object[]> {
    @Override
    public boolean isValid(Object[] value, ConstraintValidatorContext context) {
        if (value[3] == null || value[4] == null) {
            return true;
        } else {
            return ((LocalDateTime) value[4]).isAfter((LocalDateTime) value[3]);
        }
    }
}
