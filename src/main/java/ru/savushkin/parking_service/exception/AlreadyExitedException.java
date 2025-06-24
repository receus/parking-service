package ru.savushkin.parking_service.exception;

public class AlreadyExitedException extends RuntimeException {
    public AlreadyExitedException(String message) {
        super(message);
    }
}
