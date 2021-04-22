package edu.phystech.jdbcdemo.service.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import edu.phystech.jdbcdemo.service.db.DbInit;
import edu.phystech.jdbcdemo.service.db.MigrateData;
import edu.phystech.jdbcdemo.service.db.SimpleJdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskTest {
    private SimpleJdbcTemplate source;
    private AirportsDao dao;
    private FlightsDao flightsDao;

    TaskTest() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/");
        config.setUsername("postgres");
        config.setPassword("petrov");
        source = new SimpleJdbcTemplate(new HikariDataSource(config));
        dao = new AirportsDao(source);
        flightsDao = new FlightsDao(source);
    }

    @BeforeEach
    void Migrate() throws SQLException, IOException {
        new DbInit(source).create();
        try {
            new MigrateData(source).migrate();
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    @Test
    void getCities() throws SQLException, IOException {
        Set<String> cities = dao.getCity();
        Set<String> testCities = new HashSet<String>();
        testCities.add("\"{\"\"en\"\": \"\"Moscow\"\", \"\"ru\"\": \"\"Москва\"\"}\"");
        testCities.add("\"{\"\"en\"\": \"\"Ulyanovsk\"\", \"\"ru\"\": \"\"Ульяновск\"\"}\"");
        assertEquals(new HashSet<>(testCities), cities);
    }

    @Test
    void getCancelledCitiesTest() throws SQLException {
        Map<String, Integer> cities = flightsDao.getCancelledFlights();
        Map<String, Integer> citiesTest = new HashMap<>();
        citiesTest.put("\"{\"\"en\"\": \"\"Moscow\"\", \"\"ru\"\": \"\"Москва\"\"}\"", 77);
        citiesTest.put("\"{\"\"en\"\": \"\"St. Petersburg\"\", \"\"ru\"\": \"\"Санкт-Петербург\"\"}\"", 15);
        citiesTest.put("\"{\"\"en\"\": \"\"Novosibirsk\"\", \"\"ru\"\": \"\"Новосибирск\"\"}\"", 10);
        citiesTest.put("\"{\"\"en\"\": \"\"Krasnoyarsk\"\", \"\"ru\"\": \"\"Красноярск\"\"}\"", 10);

        for (Map.Entry<String, Integer> entry : citiesTest.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            assertTrue(cities.get(key).equals(value));
        }
    }

    @Test
    void getCancelledPerMonthTest() throws SQLException {
        Map<Integer, Integer> cities = flightsDao.getMonthCancelled();
        Map<Integer, Integer> monthTest = new HashMap<>();
        monthTest.put(9, 403);
        monthTest.put(8, 7);
        monthTest.put(7, 4);

        for (Map.Entry<Integer, Integer> entry : monthTest.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            assertTrue(cities.get(key).equals(value));
        }
    }

    @Test
    void getArrivalMoscowTest() throws SQLException {
        Map<String, Integer> moscowArrival = flightsDao.getDailyMoscowArrival();
        Map<String, Integer> monthTest = new HashMap<>();
        monthTest.put("TUESDAY",1224);
        monthTest.put("WEDNESDAY",1116);
        monthTest.put("FRIDAY",976);
        monthTest.put("THURSDAY",1215);
        monthTest.put("MONDAY",1134);
        monthTest.put("SUNDAY",1170);
        monthTest.put("SATURDAY",1081);
        assertEquals(moscowArrival, monthTest);
    }

    @Test
    void getDepartureMoscowTest() throws SQLException {
        Map<String, Integer> moscowArrival = flightsDao.getDailyMoscowDeparture();
        Map<String, Integer> monthTest = new HashMap<>();
        monthTest.put("TUESDAY",1215);
        monthTest.put("WEDNESDAY",1116);
        monthTest.put("FRIDAY",992);
        monthTest.put("THURSDAY",1197);
        monthTest.put("MONDAY",1143);
        monthTest.put("SUNDAY",1206);
        monthTest.put("SATURDAY",1048);
        assertEquals(moscowArrival, monthTest);
    }
}