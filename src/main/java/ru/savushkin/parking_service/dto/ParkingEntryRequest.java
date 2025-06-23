package ru.savushkin.parking_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.savushkin.parking_service.entry.VehicleType;

public record ParkingEntryRequest(
        @NotBlank String vehicleNumber,
        @NotNull VehicleType vehicleType
) {
}
