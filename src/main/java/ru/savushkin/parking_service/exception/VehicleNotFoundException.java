package ru.savushkin.parking_service.exception;

public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException(String vehicleNumber) {
        super("Vehicle not found on parking: " + vehicleNumber);
    }
}
