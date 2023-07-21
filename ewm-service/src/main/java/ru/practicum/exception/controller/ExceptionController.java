package ru.practicum.exception.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exception.*;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handle(NoSuchElementException exc) {
        return new ApiError(
                null,
                exc.getMessage(),
                "Entity wasn't created or was deleted",
                "404",
                LocalDateTime.now());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handle(DataIntegrityViolationException exc) {
        return new ApiError(
                null,
                exc.getMessage(),
                "Entity already exists.",
                "409",
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(EventParticipationForbiddenException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handle(EventParticipationForbiddenException exc) {
        return new ApiError(
                null,
                exc.getMessage(),
                "Participation is forbidden.",
                "409",
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(ChangeEventStatusForbiddenException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handle(ChangeEventStatusForbiddenException exc) {
        return new ApiError(
                null,
                exc.getMessage(),
                "Changing event status is forbidden.",
                "409",
                LocalDateTime.now()
        );
    }


    @ExceptionHandler(RequestStatusNotPendingException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handle(RequestStatusNotPendingException exc) {
        return new ApiError(
                null,
                exc.getMessage(),
                "Changing event status is forbidden.",
                "409",
                LocalDateTime.now()
        );
    }


    @ExceptionHandler(RequestNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handle(RequestNotValidException exc) {
        return new ApiError(
                null,
                exc.getMessage(),
                "Request not valid.",
                "400",
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handle(ConstraintViolationException exc) {
        return new ApiError(
                null,
                exc.getMessage(),
                "Request not valid.",
                "400",
                LocalDateTime.now()
        );
    }
}
