package ru.savushkin.parking_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.savushkin.parking_service.entity.ParkingSession;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ParkingRepository extends JpaRepository<ParkingSession, UUID> {

    Optional<ParkingSession> findByVehicleNumberAndParkedTrue(String vehicleNumber);
}
