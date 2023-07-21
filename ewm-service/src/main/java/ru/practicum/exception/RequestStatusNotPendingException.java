package ru.practicum.exception;

public class RequestStatusNotPendingException extends RuntimeException {
    public RequestStatusNotPendingException() {
    }

    public RequestStatusNotPendingException(String message) {
        super(message);
    }
}
