package edu.phystech.jdbcdemo.service.db;

import edu.phystech.jdbcdemo.service.dao.*;

import java.io.IOException;
import java.sql.SQLException;

public class MigrateData {
    private final SimpleJdbcTemplate source;
    private final AircraftsDao aircraftsDao;
    private final AirportsDao airportsDao;
    private final BoardingPassesDao boardingPassesDao;
    private final BookingsDao bookingsDao;
    private final FlightsDao flightsDao;
    private final SeatsDao seatsDao;
    private final TicketFlightsDao ticketFlightsDao;
    private final TicketsDao ticketsDao;

    public MigrateData(SimpleJdbcTemplate _source) {
        source = _source;
        airportsDao = new AirportsDao(source);
        aircraftsDao = new AircraftsDao(source);
        boardingPassesDao = new BoardingPassesDao(source);
        bookingsDao = new BookingsDao(source);
        flightsDao = new FlightsDao(source);
        seatsDao = new SeatsDao(source);
        ticketFlightsDao = new TicketFlightsDao(source);
        ticketsDao = new TicketsDao(source);
    }

    public void migrate() throws SQLException, IOException {
        if (aircraftsDao.getAircraftCount() == 0) {
            aircraftsDao.saveAircrafts(DbData.getTestAircrafts());
        }
        if (airportsDao.getAirportsCount() == 0) {
            airportsDao.saveAirports(DbData.getTestAirports());
        }
        if (bookingsDao.getBookingsCount() == 0) {
            bookingsDao.saveBookings(DbData.getTestBookings());
        }
        if (flightsDao.getFlightsCount() == 0) {
            flightsDao.saveFlights(DbData.getTestFlights());
        }
        if (seatsDao.getSeatsCount() == 0) {
            seatsDao.saveSeats(DbData.getTestSeats());
        }
        if (ticketsDao.getTicketsCount() == 0) {
            ticketsDao.saveTickets(DbData.getTestTickets());
        }
        if (ticketFlightsDao.getTicketFlightsCount() == 0) {
            ticketFlightsDao.saveTicketFlights(DbData.getTestTicketFlights());
        }
        if (boardingPassesDao.getBoardingPassesCount() == 0) {
            boardingPassesDao.saveBoardingPasses(DbData.getTestBoardingPasses());
        }
    }
}
