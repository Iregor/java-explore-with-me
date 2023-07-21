package ru.practicum.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = InFutureValidator.class)
public @interface InFuture {
    int value() default 0;

    String message() default "{Date should be after now with value offset}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
