package edu.phystech.jdbcdemo.domain;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class Flights {
    private final int flight_id;
    private final String flight_no;
    private final OffsetDateTime scheduled_departure;
    private final OffsetDateTime scheduled_arrival;
    private final String departure_airport;
    private final String arrival_airport;
    private final String status;
    private final String aircraft_code;
    private final OffsetDateTime actual_departure;
    private final OffsetDateTime actual_arrival;
}
