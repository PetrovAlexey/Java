package edu.phystech.jdbcdemo.service.dao;

import edu.phystech.jdbcdemo.domain.*;
import edu.phystech.jdbcdemo.service.db.SimpleJdbcTemplate;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@AllArgsConstructor
public class AircraftsDao {
    private final SimpleJdbcTemplate source;

    private Aircrafts createAircrafts(ResultSet resultSet) throws SQLException {
        return new Aircrafts(resultSet.getString("aircrafr_code"),
                resultSet.getString("model"),
                resultSet.getInt("range"));
    }

    public void saveAircrafts(Collection<Aircrafts> aircrafts) throws SQLException {
        source.preparedStatement("insert into aircrafts(aircraft_code, model, range) values (?, ?, ?)", insertAircraft -> {
            for (Aircrafts conference : aircrafts) {
                insertAircraft.setString(1, conference.getAircraft_code());
                insertAircraft.setString(2, conference.getModel());
                insertAircraft.setInt(3, conference.getRange());
                insertAircraft.execute();
            }
        });
    }

    public int getAircraftCount() throws SQLException {
        return source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("select count(*) from aircrafts");
            resultSet.next();
            return resultSet.getInt(1);
        });
    }

    public Set<Aircrafts> getAircrafts() throws SQLException {
        return source.statement(stmt -> {
            Set<Aircrafts> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("select * from aircrafts");
            while (resultSet.next()) {
                result.add(createAircrafts(resultSet));
            }
            return result;
        });
    }
}
