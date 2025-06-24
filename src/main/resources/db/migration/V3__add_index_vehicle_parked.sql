CREATE index idx_parking_vehicle_parked
    ON parking_session(vehicle_number, parked);
