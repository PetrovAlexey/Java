package edu.phystech.jdbcdemo.domain;

import lombok.Data;

import java.sql.Date;
import java.time.OffsetDateTime;

@Data
public class Bookings {
    private final String book_ref;
    private final OffsetDateTime book_date;
    private final double total_amount;
}
