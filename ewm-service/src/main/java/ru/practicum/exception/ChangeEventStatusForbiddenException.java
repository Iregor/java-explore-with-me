package ru.practicum.exception;

public class ChangeEventStatusForbiddenException extends RuntimeException {
    public ChangeEventStatusForbiddenException() {
    }

    public ChangeEventStatusForbiddenException(String message) {
        super(message);
    }
}
