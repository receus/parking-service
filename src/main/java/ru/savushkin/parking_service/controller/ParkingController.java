package ru.savushkin.parking_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.savushkin.parking_service.dto.ParkingEntryRequest;
import ru.savushkin.parking_service.dto.ParkingEntryResponse;
import ru.savushkin.parking_service.service.ParkingService;

@RestController
@RequestMapping("v1/parking")
@RequiredArgsConstructor
public class ParkingController {

    private final ParkingService parkingService;

    @PostMapping("/entry")
    @Operation(summary = "Регистрация въезда автомобиля")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Въезд успешно зарегистрирован"),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации запроса"),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<ParkingEntryResponse> parkingEntry(@RequestBody @Valid ParkingEntryRequest request) {
        return ResponseEntity.ok(parkingService.registerEntry(request));
    }
}
