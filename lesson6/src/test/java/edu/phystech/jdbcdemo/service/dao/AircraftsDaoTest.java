package edu.phystech.jdbcdemo.service.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import edu.phystech.jdbcdemo.domain.Aircrafts;
import edu.phystech.jdbcdemo.service.db.DbData;
import edu.phystech.jdbcdemo.service.db.DbInit;
import edu.phystech.jdbcdemo.service.db.MigrateData;
import edu.phystech.jdbcdemo.service.db.SimpleJdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class AircraftsDaoTest {
    private SimpleJdbcTemplate source;
    private AircraftsDao dao;

    AircraftsDaoTest() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/");
        config.setUsername("postgres");
        config.setPassword("petrov");
        source = new SimpleJdbcTemplate(new HikariDataSource(config));
        dao = new AircraftsDao(source);
    }

    @BeforeEach
    void setupDB() throws IOException, SQLException {
         new DbInit(source).create();
         new MigrateData(source).migrate();
    }

    @Test
    void getConferences() throws SQLException, IOException {
        if (dao.getAircraftCount() != 0)
            return;
        Collection<Aircrafts> testAircrafts = DbData.getTestAircrafts();
        dao.saveAircrafts(testAircrafts);
        Set<Aircrafts> aircrafts = dao.getAircrafts();
        assertNotSame(testAircrafts, aircrafts);
        assertEquals(new HashSet<>(testAircrafts), aircrafts);
    }
}
