package ru.savushkin.parking_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.savushkin.parking_service.config.ParkingProperties;
import ru.savushkin.parking_service.dto.ParkingEntryRequest;
import ru.savushkin.parking_service.dto.ParkingExitRequest;
import ru.savushkin.parking_service.dto.ParkingReportResponse;
import ru.savushkin.parking_service.entity.ParkingSession;
import ru.savushkin.parking_service.entity.VehicleType;
import ru.savushkin.parking_service.exception.AlreadyParkedException;
import ru.savushkin.parking_service.exception.VehicleNotFoundException;
import ru.savushkin.parking_service.repository.ParkingRepository;
import ru.savushkin.parking_service.service.impl.ParkingServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ParkingServiceImplTest {

    private final ParkingRepository parkingRepository = mock(ParkingRepository.class);
    private final ParkingProperties parkingProperties = mock(ParkingProperties.class);
    private final ParkingService parkingService = new ParkingServiceImpl(parkingRepository, parkingProperties);

    @Test
    void registerEntry_shouldSaveSessionAndReturnResponse() {
        ParkingEntryRequest request = new ParkingEntryRequest("О700АО_70RUS", VehicleType.CAR);
        var response = parkingService.registerEntry(request);

        ArgumentCaptor<ParkingSession> captor = ArgumentCaptor.forClass(ParkingSession.class);
        verify(parkingRepository, times(1)).save(captor.capture());

        ParkingSession session = captor.getValue();
        assertThat(session.getVehicleNumber()).isEqualTo("О700АО_70RUS".toUpperCase(Locale.ROOT));
        assertThat(session.getVehicleType()).isEqualTo(VehicleType.CAR);
        assertThat(response.sessionId()).isEqualTo(session.getId());
        assertThat(response.entryTime()).isEqualTo(session.getEntryTime());
    }

    @Test
    void registerEntry_shouldThrowIfVehicleAlreadyParked() {
        String vehicleNumber = "О700АО_70RUS";
        ParkingSession existing = ParkingSession.builder()
                .id(UUID.randomUUID())
                .vehicleNumber(vehicleNumber)
                .vehicleType(VehicleType.CAR)
                .entryTime(LocalDateTime.now().minusMinutes(30))
                .parked(true)
                .build();

        when(parkingRepository.findByVehicleNumberAndParkedTrue(vehicleNumber))
                .thenReturn(Optional.of(existing));

        ParkingEntryRequest request = new ParkingEntryRequest(vehicleNumber, VehicleType.CAR);

        assertThrows(AlreadyParkedException.class, () ->
                parkingService.registerEntry(request));

        verify(parkingRepository, never()).save(any());
    }

    @Test
    void registerEntry_shouldFailOnInvalidVehicleType() {
        String json = """
        {
            "vehicleNumber": "О700АО_70RUS",
            "vehicleType": "moped"
        }
        """;

        ObjectMapper mapper = new ObjectMapper();

        assertThatThrownBy(() ->
                mapper.readValue(json, ParkingEntryRequest.class)
        )
                .isInstanceOf(InvalidFormatException.class)
                .hasMessageContaining("moped");
    }

    @Test
    void registerExit_shouldSaveSessionAndReturnResponse() {
        var session = ParkingSession.builder()
                .id(UUID.randomUUID())
                .vehicleNumber("О700АО_70RUS")
                .vehicleType(VehicleType.CAR)
                .entryTime(LocalDateTime.now().minusHours(1))
                .parked(true)
                .build();

        when(parkingRepository.findByVehicleNumberAndParkedTrue("О700АО_70RUS"))
                .thenReturn(Optional.of(session));

        var response = parkingService.registerExit(new ParkingExitRequest("О700АО_70RUS"));

        assertNotNull(response.exitTime());
        assertFalse(session.isParked());
        verify(parkingRepository).save(session);
    }

    @Test
    void registerExit_shouldThrowIfVehicleNotFound() {
        when(parkingRepository.findByVehicleNumberAndParkedTrue("О700АО_70RUS"))
                .thenReturn(Optional.empty());

        assertThrows(VehicleNotFoundException.class, () ->
                parkingService.registerExit(new ParkingExitRequest("О700АО_70RUS")));

        verify(parkingRepository, never()).save(any());
    }

    @Test
    void getReport_shouldReturnCorrectValues() {
        LocalDateTime start = LocalDateTime.of(2025, 6, 23, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, 6, 24, 23, 59, 59);

        when(parkingRepository.countCurrentlyOccupied()).thenReturn(30);
        when(parkingRepository.findAverageDurationMinutes(start, end))
                .thenReturn(BigDecimal.valueOf(120.456));

        when(parkingProperties.getParkingCapacity()).thenReturn(100);

        ParkingReportResponse response = parkingService.getReport(start, end);

        assertEquals(30, response.occupiedPlaces());
        assertEquals(70, response.freePlaces());
        assertEquals(BigDecimal.valueOf(120.46), response.averageOccupied());
    }
}

