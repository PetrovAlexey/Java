package edu.phystech.jdbcdemo.service.dao;

import edu.phystech.jdbcdemo.domain.Flights;
import edu.phystech.jdbcdemo.service.db.SimpleJdbcTemplate;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

@AllArgsConstructor
public class FlightsDao {
    private final SimpleJdbcTemplate source;
    private Flights createFlights(ResultSet resultSet) throws SQLException {
        return new Flights(resultSet.getInt("flight_id"),
                resultSet.getString("flight_no"),
                OffsetDateTime.parse(resultSet.getString("scheduled_departure")),
                OffsetDateTime.parse(resultSet.getString("scheduled_arrival")),
                resultSet.getString("departure_airport"),
                resultSet.getString("arrival_airport"),
                resultSet.getString("status"),
                resultSet.getString("aircraft_code"),
                OffsetDateTime.parse(resultSet.getString("actual_departure")),
                OffsetDateTime.parse(resultSet.getString("actual_arrival")));
    }

    public void saveFlights(Collection<Flights> flights) throws SQLException {
        source.preparedStatement("insert into flights(flight_id, flight_no, scheduled_departure, scheduled_arrival, departure_airport, arrival_airport, status, aircraft_code, actual_departure, actual_arrival) values (?, ?, ?, ?, ?, ?, ?, ? ,? ,?)",
                insertFlights -> {
            for (Flights flight : flights) {
                insertFlights.setInt(1, flight.getFlight_id());
                insertFlights.setString(2, flight.getFlight_no());
                insertFlights.setTimestamp(3, Timestamp.valueOf(flight.getScheduled_departure().atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()));
                insertFlights.setTimestamp(4, Timestamp.valueOf(flight.getScheduled_arrival().atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()));
                insertFlights.setString(5, flight.getDeparture_airport());
                insertFlights.setString(6, flight.getArrival_airport());
                insertFlights.setString(7, flight.getStatus());
                insertFlights.setString(8, flight.getAircraft_code());
                insertFlights.setTimestamp(9, flight.getActual_departure() == null ? null : Timestamp.valueOf(flight.getActual_departure().atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()));
                insertFlights.setTimestamp(10, flight.getActual_arrival() == null ? null : Timestamp.valueOf(flight.getActual_arrival().atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()));
                insertFlights.execute();
            }
        });
    }

    public int getFlightsCount() throws SQLException {
        return source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("select count(*) from flights");
            resultSet.next();
            return resultSet.getInt(1);
        });
    }

    public Map<String, Integer> getDailyMoscowArrival() throws SQLException {
        return source.statement(stmt -> {
            Map<String, Integer> result = new HashMap<>();
            ResultSet resultSet = stmt.executeQuery(
                    "select TO_CHAR(scheduled_departure , 'DAY') AS month, COUNT(*) AS flights \n" +
                            "from(\n" +
                            "(select airport_code from airports where city = '\"{\"\"en\"\": \"\"Moscow\"\", \"\"ru\"\": \"\"Москва\"\"}\"') \n" +
                            "as t\n" +
                            "inner join flights as f\n" +
                            "on f.arrival_airport = t.airport_code\n" +
                            ")\n" +
                            "GROUP BY TO_CHAR(scheduled_departure, 'DAY');");
            while (resultSet.next()) {
                result.put(resultSet.getString(1).replaceAll(" ", ""), resultSet.getInt(2));
            }
            return result;
        });
    }

    public Map<String, Integer> getDailyMoscowDeparture() throws SQLException {
        return source.statement(stmt -> {
            Map<String, Integer> result = new HashMap<>();
            ResultSet resultSet = stmt.executeQuery(
                    "select TO_CHAR(scheduled_departure , 'DAY') AS month, COUNT(*) AS flights \n" +
                            "from(\n" +
                            "(select airport_code from airports where city = '\"{\"\"en\"\": \"\"Moscow\"\", \"\"ru\"\": \"\"Москва\"\"}\"') \n" +
                            "as t\n" +
                            "inner join flights as f\n" +
                            "on f.departure_airport = t.airport_code\n" +
                            ")\n" +
                            "GROUP BY TO_CHAR(scheduled_departure, 'DAY');");
            while (resultSet.next()) {
                result.put(resultSet.getString(1).replaceAll(" ", ""), resultSet.getInt(2));
            }
            return result;
        });
    }

    public Map<Integer, Integer> getMonthCancelled() throws SQLException {
        return source.statement(stmt -> {
            Map<Integer, Integer> result = new HashMap<>();
            ResultSet resultSet = stmt.executeQuery(
                    "SELECT TO_CHAR(scheduled_departure , 'MM') AS month, COUNT(*) AS cancelled\n" +
                    "FROM flights\n" +
                    "where status = 'Cancelled'\n" +
                    "GROUP BY TO_CHAR(scheduled_departure, 'MM');");
            while (resultSet.next()) {
                result.put(resultSet.getInt(1), resultSet.getInt(2));
            }
            return result;
        });
    }

    public Map<String, Integer> getCancelledFlights() throws SQLException {
        return source.statement(stmt -> {
            Map<String, Integer> result = new HashMap<String, Integer>();
            ResultSet resultSet = stmt.executeQuery("select city, sum(cnt) as s from \n" +
                    "((select departure_airport, count(departure_airport) as cnt\n" +
                    "from flights \n" +
                    "where status like 'Cancelled'\n" +
                    "group by departure_airport )\n" +
                    "as t\n" +
                    "inner join \n" +
                    "airports as b\n" +
                    "on t.departure_airport = b.airport_code)\n" +
                    "group by city \n" +
                    "order by s\n" +
                    "desc");
            while (resultSet.next()) {
                result.put(resultSet.getString(1), resultSet.getInt(2));
            }
            return result;
        });
    }

    public Set<Flights> getFlights() throws SQLException {
        return source.statement(stmt -> {
            Set<Flights> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("select * from flights");
            while (resultSet.next()) {
                result.add(createFlights(resultSet));
            }
            return result;
        });
    }
}

