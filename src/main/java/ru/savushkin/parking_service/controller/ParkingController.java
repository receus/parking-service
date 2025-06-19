package ru.savushkin.parking_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.savushkin.parking_service.exception.VehicleNotFoundException;

@RestController
@RequestMapping
public class ParkingController {

    @GetMapping("/fail")
    public void fail() {
        throw new VehicleNotFoundException("12345");
    }
}
