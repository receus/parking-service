package ru.savushkin.parking_service.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ParkingEntryResponse(
        UUID sessionId,
        LocalDateTime entryTime
) {
}
