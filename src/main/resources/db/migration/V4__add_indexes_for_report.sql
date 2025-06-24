CREATE index idx_parking_session_parked
    ON parking_session(parked);
CREATE index idx_parking_session_exit_time
    ON parking_session(exit_time);
