package edu.phystech.jdbcdemo.service.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import edu.phystech.jdbcdemo.domain.Tickets;
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

public class TicketsDaoTest {
    private SimpleJdbcTemplate source;
    private TicketsDao dao;

    TicketsDaoTest() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/");
        config.setUsername("postgres");
        config.setPassword("petrov");
        source = new SimpleJdbcTemplate(new HikariDataSource(config));
        dao = new TicketsDao(source);
    }

    @BeforeEach
    void setupDB() throws IOException, SQLException {
        new DbInit(source).create();
        new MigrateData(source).migrate();
    }

    //@AfterEach
    void tearDownDB() throws SQLException, IOException {
        source.statement(stmt -> {
            stmt.execute("DROP TABLE IF EXISTS tickets CASCADE");
        });
    }

    @Test
    void getTickets() throws SQLException, IOException {
        if (dao.getTicketsCount() != 0)
            return;
        Collection<Tickets> testTickets = DbData.getTestTickets();
        dao.saveTickets(testTickets);
        Set<Tickets> tickets = dao.getTickets();
        assertNotSame(testTickets, tickets);
        assertEquals(new HashSet<>(testTickets), tickets);
    }
}
