package ru.practicum.exception;

public class EventParticipationForbiddenException extends RuntimeException {
    public EventParticipationForbiddenException() {
    }

    public EventParticipationForbiddenException(String message) {
        super(message);
    }
}
