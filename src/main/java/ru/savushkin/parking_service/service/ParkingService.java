package ru.savushkin.parking_service.service;

import jakarta.validation.Valid;
import ru.savushkin.parking_service.dto.ParkingEntryRequest;
import ru.savushkin.parking_service.dto.ParkingEntryResponse;
import ru.savushkin.parking_service.dto.ParkingExitRequest;
import ru.savushkin.parking_service.dto.ParkingExitResponse;

public interface ParkingService {

    ParkingEntryResponse registerEntry(ParkingEntryRequest request);

    ParkingExitResponse registerExit(@Valid ParkingExitRequest request);
}
