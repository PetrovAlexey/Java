package edu.phystech.jdbcdemo.service.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import edu.phystech.jdbcdemo.domain.TicketFlights;
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

public class TicketFlightsDaoTest {
    private SimpleJdbcTemplate source;
    private TicketFlightsDao dao;

    TicketFlightsDaoTest() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/");
        config.setUsername("postgres");
        config.setPassword("petrov");
        source = new SimpleJdbcTemplate(new HikariDataSource(config));
        dao = new TicketFlightsDao(source);
    }

    @BeforeEach
    void setupDB() throws IOException, SQLException {
        new DbInit(source).create();
        new MigrateData(source).migrate();
    }

    //@AfterEach
    void tearDownDB() throws SQLException, IOException {
        source.statement(stmt -> {
            stmt.execute("DROP TABLE IF EXISTS ticket_flights CASCADE");
        });
    }

    @Test
    void getTicketFlights() throws SQLException, IOException {
        if (dao.getTicketFlightsCount() != 0)
            return;
        Collection<TicketFlights> testTicketFlights = DbData.getTestTicketFlights();
        dao.saveTicketFlights(testTicketFlights);
        Set<TicketFlights> ticketFlights = dao.getTicketFlights();
        assertNotSame(testTicketFlights, ticketFlights);
        assertEquals(new HashSet<>(testTicketFlights), ticketFlights);
    }
}
