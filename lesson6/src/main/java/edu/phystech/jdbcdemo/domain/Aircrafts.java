package edu.phystech.jdbcdemo.domain;

import lombok.Data;

@Data
public class Aircrafts  {
    private final String aircraft_code;
    private final String model;
    private final int range;
}
