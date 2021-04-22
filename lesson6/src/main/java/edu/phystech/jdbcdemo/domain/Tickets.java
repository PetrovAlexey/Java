package edu.phystech.jdbcdemo.domain;

import lombok.Data;

@Data
public class Tickets {
    private final String ticket_no;
    private final String book_ref;
    private final String passenger_id;
    private final String passenger_name;
    private final String contact_data;
}
