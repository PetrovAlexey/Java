package edu.phystech.jdbcdemo.service.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import edu.phystech.jdbcdemo.domain.Flights;
import edu.phystech.jdbcdemo.service.db.DbData;
import edu.phystech.jdbcdemo.service.db.DbInit;
import edu.phystech.jdbcdemo.service.db.MigrateData;
import edu.phystech.jdbcdemo.service.db.SimpleJdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

public class FlightsDaoTest {
    private SimpleJdbcTemplate source;
    private FlightsDao dao;

    FlightsDaoTest() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/");
        config.setUsername("postgres");
        config.setPassword("petrov");
        source = new SimpleJdbcTemplate(new HikariDataSource(config));
        dao = new FlightsDao(source);
    }

    @BeforeEach
    void setupDB() throws IOException, SQLException {
        new DbInit(source).create();
        new MigrateData(source).migrate();
    }

    //@AfterEach
    void tearDownDB() throws SQLException, IOException {
        source.statement(stmt -> {
            stmt.execute("DROP TABLE IF EXISTS flights CASCADE");
        });
    }

    @Test
    void getFlights() throws SQLException, IOException {
        if (dao.getFlightsCount() != 0)
            return;
        Collection<Flights> testFlights = DbData.getTestFlights();
        Set<Flights> flights = dao.getFlights();
        assertNotSame(testFlights, flights);
        assertEquals(new HashSet<>(testFlights), flights);
    }
}
