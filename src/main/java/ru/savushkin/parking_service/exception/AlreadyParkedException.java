package ru.savushkin.parking_service.exception;

public class AlreadyParkedException extends RuntimeException {
    public AlreadyParkedException(String message) {
        super(message);
    }
}
