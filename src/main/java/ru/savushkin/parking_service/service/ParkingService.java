package ru.savushkin.parking_service.service;

import ru.savushkin.parking_service.dto.ParkingEntryRequest;
import ru.savushkin.parking_service.dto.ParkingEntryResponse;

public interface ParkingService {

    ParkingEntryResponse registerEntry(ParkingEntryRequest request);
}
