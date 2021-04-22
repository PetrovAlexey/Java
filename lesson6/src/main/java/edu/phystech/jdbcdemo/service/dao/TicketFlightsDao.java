package edu.phystech.jdbcdemo.service.dao;
import edu.phystech.jdbcdemo.domain.TicketFlights;
import edu.phystech.jdbcdemo.service.db.SimpleJdbcTemplate;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class TicketFlightsDao {
    private final SimpleJdbcTemplate source;
    private TicketFlights createTicketFlights(ResultSet resultSet) throws SQLException {
        return new TicketFlights(resultSet.getString("ticket_no"),
                resultSet.getInt("flight_id"),
                resultSet.getString("fare_conditions"),
                resultSet.getDouble("amount"));
    }

    public void saveTicketFlights(Collection<TicketFlights> ticketFlights) throws SQLException {
        source.preparedStatement("insert into ticket_flights(ticket_no, flight_id, fare_conditions, amount) values (?, ?, ?, ?)", insertBookings -> {
            for (TicketFlights ticketFlight : ticketFlights) {
                insertBookings.setString(1, ticketFlight.getTicket_no());
                insertBookings.setInt(2, ticketFlight.getFlight_id());
                insertBookings.setString(3, ticketFlight.getFare_conditions());
                insertBookings.setDouble(4, ticketFlight.getAmount());
                insertBookings.execute();
            }
        });
    }

    public int getTicketFlightsCount() throws SQLException {
        return source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("select count(*) from ticket_flights");
            resultSet.next();
            return resultSet.getInt(1);
        });
    }

    public Set<TicketFlights> getTicketFlights() throws SQLException {
        return source.statement(stmt -> {
            Set<TicketFlights> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("select * from ticket_flights");
            while (resultSet.next()) {
                result.add(createTicketFlights(resultSet));
            }
            return result;
        });
    }
}
