package ru.savushkin.parking_service.dto;

import java.time.LocalDateTime;

public record ParkingExitResponse(
    LocalDateTime exitTime
) {
}
