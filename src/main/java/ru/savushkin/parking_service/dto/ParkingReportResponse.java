package ru.savushkin.parking_service.dto;

import java.math.BigDecimal;

public record ParkingReportResponse(
        int occupiedPlaces,
        int freePlaces,
        BigDecimal averageOccupied
) {
}
