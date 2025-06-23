package ru.savushkin.parking_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.savushkin.parking_service.dto.ParkingEntryRequest;
import ru.savushkin.parking_service.entry.ParkingSession;
import ru.savushkin.parking_service.entry.VehicleType;
import ru.savushkin.parking_service.repository.ParkingRepository;
import ru.savushkin.parking_service.service.impl.ParkingServiceImpl;

import java.util.Locale;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ParkingServiceImplTest {

    private final ParkingRepository parkingRepository = mock(ParkingRepository.class);
    private final ParkingService parkingService = new ParkingServiceImpl(parkingRepository);

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
    void registerEntry_shouldFailOnInvalidVehicleType() {
        String json = """
        {
            "vehicleNumber": "A123BC",
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
}

