package ru.savushkin.parking_service.service;

import jakarta.validation.Valid;
import ru.savushkin.parking_service.dto.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ParkingService {

    ParkingEntryResponse registerEntry(ParkingEntryRequest request);

    ParkingExitResponse registerExit(@Valid ParkingExitRequest request);

    ParkingReportResponse getReport(LocalDateTime start, LocalDateTime end);
}
