package edu.phystech.jdbcdemo.domain;

import lombok.Data;

@Data
public class Airports {
    private final String airport_code;
    private final String airport_name;
    private final String city;
    private final String coordinates;
    private final String timezone;
}
