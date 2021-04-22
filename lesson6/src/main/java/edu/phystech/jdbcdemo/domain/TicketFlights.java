package edu.phystech.jdbcdemo.domain;

import lombok.Data;

@Data
public class TicketFlights {
    private final String ticket_no;
    private final int flight_id;
    private final String fare_conditions;
    private final Double amount;
}
