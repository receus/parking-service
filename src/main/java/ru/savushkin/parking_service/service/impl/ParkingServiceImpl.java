package ru.savushkin.parking_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.savushkin.parking_service.dto.ParkingEntryRequest;
import ru.savushkin.parking_service.dto.ParkingEntryResponse;
import ru.savushkin.parking_service.dto.ParkingExitRequest;
import ru.savushkin.parking_service.dto.ParkingExitResponse;
import ru.savushkin.parking_service.entity.ParkingSession;
import ru.savushkin.parking_service.exception.AlreadyExitedException;
import ru.savushkin.parking_service.exception.AlreadyParkedException;
import ru.savushkin.parking_service.exception.VehicleNotFoundException;
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
        String normalizedNumber = request.vehicleNumber().toUpperCase(Locale.ROOT);

        repository.findByVehicleNumberAndParkedTrue(normalizedNumber)
                .ifPresent(session -> {
                    throw new AlreadyParkedException("Vehicle already parked: " + normalizedNumber);
                });

        ParkingSession session = ParkingSession.builder()
                .id(UUID.randomUUID())
                .vehicleNumber(normalizedNumber)
                .vehicleType(request.vehicleType())
                .entryTime(LocalDateTime.now())
                .parked(true)
                .build();

        repository.save(session);
        log.info("Въезд зарегистрирован: {} ({})", session.getVehicleNumber(), session.getEntryTime());

        return new ParkingEntryResponse(session.getId(), session.getEntryTime());
    }

    @Override
    public ParkingExitResponse registerExit(ParkingExitRequest request) {
        String normalizedNumber = request.vehicleNumber().toUpperCase(Locale.ROOT);

        ParkingSession session = repository.findByVehicleNumberAndParkedTrue(normalizedNumber)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found on parking: " + normalizedNumber));

        if (!session.isParked()) {
            throw new AlreadyExitedException("Vehicle already exited: " + normalizedNumber);
        }
        session.setExitTime(LocalDateTime.now());
        session.setParked(false);

        repository.save(session);
        log.info("Выезд зарегистрирован: {} ({})", session.getVehicleNumber(), session.getExitTime());

        return new ParkingExitResponse(session.getExitTime());
    }
}
