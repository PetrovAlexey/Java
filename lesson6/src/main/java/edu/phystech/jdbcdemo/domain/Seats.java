package edu.phystech.jdbcdemo.domain;

import lombok.Data;

@Data
public class Seats {
    private final String aircraft_code;
    private final String seat_no;
    private final String fare_conditions;
}
