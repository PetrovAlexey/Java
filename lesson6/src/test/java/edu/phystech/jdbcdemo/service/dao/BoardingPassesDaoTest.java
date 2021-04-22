package edu.phystech.jdbcdemo.service.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import edu.phystech.jdbcdemo.domain.BoardingPasses;
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

public class BoardingPassesDaoTest {
    private SimpleJdbcTemplate source;
    private BoardingPassesDao dao;

    BoardingPassesDaoTest() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/");
        config.setUsername("postgres");
        config.setPassword("petrov");
        source = new SimpleJdbcTemplate(new HikariDataSource(config));
        dao = new BoardingPassesDao(source);
    }

    @BeforeEach
    void setupDB() throws IOException, SQLException {
        new DbInit(source).create();
        new MigrateData(source).migrate();
    }

    //@AfterEach
    void tearDownDB() throws SQLException, IOException {
        source.statement(stmt -> {
            stmt.execute("DROP TABLE IF EXISTS boarding_passes CASCADE");
        });
    }

    @Test
    void getBoardingPasses() throws SQLException, IOException {
        if (dao.getBoardingPassesCount() != 0)
            return;
        Collection<BoardingPasses> testBoardingPasses = DbData.getTestBoardingPasses();
        Set<BoardingPasses> boardingPasses = dao.getBoardingPasses();
        assertNotSame(testBoardingPasses, boardingPasses);
        assertEquals(new HashSet<>(testBoardingPasses), boardingPasses);
    }
}
