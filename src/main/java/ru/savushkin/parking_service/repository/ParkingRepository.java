package ru.savushkin.parking_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.savushkin.parking_service.entity.ParkingSession;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ParkingRepository extends JpaRepository<ParkingSession, UUID> {

    Optional<ParkingSession> findByVehicleNumberAndParkedTrue(String vehicleNumber);

    @Query("SELECT COUNT(p) from ParkingSession p WHERE p.parked = true")
    int countCurrentlyOccupied();

    @Query(value = """
                SELECT AVG(EXTRACT(EPOCH FROM (parking_session.exit_time - parking_session.entry_time)) / 60)
                FROM parking_session
                WHERE exit_time BETWEEN :start AND :end
            """, nativeQuery = true)
    BigDecimal findAverageDurationMinutes(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
