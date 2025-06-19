package ru.savushkin.parking_service.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        String message,
        LocalDateTime timestamp,
        int status
) {
}
