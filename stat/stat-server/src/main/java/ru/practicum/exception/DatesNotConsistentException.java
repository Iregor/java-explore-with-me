package ru.practicum.exception;

public class DatesNotConsistentException extends RuntimeException {

    public DatesNotConsistentException() {
    }

    public DatesNotConsistentException(String message) {
        super(message);
    }
}
