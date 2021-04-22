package edu.phystech.jdbcdemo.service.dao;

import edu.phystech.jdbcdemo.domain.Airports;
import edu.phystech.jdbcdemo.service.db.SimpleJdbcTemplate;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class AirportsDao {
    private final SimpleJdbcTemplate source;
    private Airports createAirports(ResultSet resultSet) throws SQLException {
        return new Airports(resultSet.getString("airport_code"),
                resultSet.getString("airport_name"),
                resultSet.getString("city"),
                resultSet.getString("coordinates"),
                resultSet.getString("timezone"));
    }

    public void saveAirports(Collection<Airports> airports) throws SQLException {
        source.preparedStatement("insert into airports(airport_code, airport_name, city, coordinates, timezone) values (?, ?, ?, ?, ?)", insertAirports -> {
            for (Airports airport : airports) {
                insertAirports.setString(1, airport.getAirport_code());
                insertAirports.setString(2, airport.getAirport_name());
                insertAirports.setString(3, airport.getCity());
                insertAirports.setString(4, airport.getCoordinates());
                insertAirports.setString(5, airport.getTimezone());
                insertAirports.execute();
            }
        });
    }

    public Set<String> getCity() throws SQLException {
        return source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("select city, cnt from (\n" +
                    "select city, count(*) as cnt from airports group by city) as t where cnt > 1");
            Set<String> result = new HashSet<>();
            while (resultSet.next()) {
                result.add(resultSet.getString(1));
            }
            return result;
        });
    }

    public int getAirportsCount() throws SQLException {
        return source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("select count(*) from airports");
            resultSet.next();
            return resultSet.getInt(1);
        });
    }

    public Set<Airports> getAirports() throws SQLException {
        return source.statement(stmt -> {
            Set<Airports> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("select * from airports");
            while (resultSet.next()) {
                result.add(createAirports(resultSet));
            }
            return result;
        });
    }
}
