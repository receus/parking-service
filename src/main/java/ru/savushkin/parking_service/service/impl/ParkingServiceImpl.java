package ru.savushkin.parking_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.savushkin.parking_service.dto.ParkingEntryRequest;
import ru.savushkin.parking_service.dto.ParkingEntryResponse;
import ru.savushkin.parking_service.entry.ParkingSession;
import ru.savushkin.parking_service.repository.ParkingRepository;
import ru.savushkin.parking_service.service.ParkingService;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParkingServiceImpl implements ParkingService {

    private final ParkingRepository repository;

    @Override
    public ParkingEntryResponse registerEntry(ParkingEntryRequest request) {
        ParkingSession session = ParkingSession.builder()
                .id(UUID.randomUUID())
                .vehicleNumber(request.vehicleNumber().toUpperCase(Locale.ROOT))
                .vehicleType(request.vehicleType())
                .entryTime(LocalDateTime.now())
                .parked(true)
                .build();

        repository.save(session);
        log.info("Въезд зарегистрирован: {} ({})", session.getVehicleNumber(), session.getEntryTime());

        return new ParkingEntryResponse(session.getId(), session.getEntryTime());
    }
}
