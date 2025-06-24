package ru.savushkin.parking_service.exception;

public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException(String message ) {
        super(message);
    }
}
