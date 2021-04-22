package edu.phystech.jdbcdemo.service.dao;

import edu.phystech.jdbcdemo.domain.Seats;
import edu.phystech.jdbcdemo.service.db.SimpleJdbcTemplate;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class SeatsDao {
    private final SimpleJdbcTemplate source;
    private Seats createSeats(ResultSet resultSet) throws SQLException {
        return new Seats(resultSet.getString("aircraft_code"),
                resultSet.getString("seat_no"),
                resultSet.getString("fare_conditions"));
    }

    public void saveSeats(Collection<Seats> seats) throws SQLException {
        source.preparedStatement("insert into seats(aircraft_code, seat_no, fare_conditions) values (?, ?, ?)", insertBookings -> {
            for (Seats seat : seats) {
                insertBookings.setString(1, seat.getAircraft_code());
                insertBookings.setString(2, seat.getSeat_no());
                insertBookings.setString(3, seat.getFare_conditions());
                insertBookings.execute();
            }
        });
    }

    public Set<Seats> getSeats() throws SQLException {
        return source.statement(stmt -> {
            Set<Seats> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("select * from seats");
            while (resultSet.next()) {
                result.add(createSeats(resultSet));
            }
            return result;
        });
    }

    public int getSeatsCount() throws SQLException {
        return source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("select count(*) from seats");
            resultSet.next();
            return resultSet.getInt(1);
        });
    }
}
