CREATE TABLE parking_session (
                                 id UUID PRIMARY KEY,
                                 vehicle_number VARCHAR(20) NOT NULL,
                                 vehicle_type VARCHAR(20) NOT NULL,
                                 entry_time TIMESTAMP NOT NULL,
                                 exit_time TIMESTAMP
);
