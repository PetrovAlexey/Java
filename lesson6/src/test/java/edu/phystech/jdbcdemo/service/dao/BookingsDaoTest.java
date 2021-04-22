package edu.phystech.jdbcdemo.service.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import edu.phystech.jdbcdemo.domain.Bookings;
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

public class BookingsDaoTest {
    private SimpleJdbcTemplate source;
    private BookingsDao dao;

    BookingsDaoTest() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/");
        config.setUsername("postgres");
        config.setPassword("petrov");
        source = new SimpleJdbcTemplate(new HikariDataSource(config));
        dao = new BookingsDao(source);
    }

    @BeforeEach
    void setupDB() throws IOException, SQLException {
        new DbInit(source).create();
        new MigrateData(source).migrate();
    }

    //@AfterEach
    void tearDownDB() throws SQLException, IOException {
        source.statement(stmt -> {
            stmt.execute("DROP TABLE IF EXISTS bookings CASCADE");
        });
    }

    @Test
    void getBookings() throws SQLException, IOException {
        if (dao.getBookingsCount() != 0)
            return;
        Collection<Bookings> testBookings = DbData.getTestBookings();
        Set<Bookings> bookings = dao.getBookings();
        assertNotSame(testBookings, bookings);
        assertEquals(new HashSet<>(testBookings), bookings);
    }
}
