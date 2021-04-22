package edu.phystech.jdbcdemo.domain;

import lombok.Data;

@Data
public class BoardingPasses {
    private final String ticket_no;
    private final int flight_id;
    private final int boarding_no;
    private final String seat_no;
}
