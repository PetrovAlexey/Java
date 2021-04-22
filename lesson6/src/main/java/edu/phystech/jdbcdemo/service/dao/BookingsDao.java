package edu.phystech.jdbcdemo.service.dao;

import edu.phystech.jdbcdemo.domain.Bookings;
import edu.phystech.jdbcdemo.service.db.SimpleJdbcTemplate;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class BookingsDao {
    private final SimpleJdbcTemplate source;
    private Bookings createBookings(ResultSet resultSet) throws SQLException {
        return new Bookings(resultSet.getString("book_ref"),
                OffsetDateTime.parse(resultSet.getString("book_date")),
                resultSet.getLong("total_amount"));
    }

    public void saveBookings(Collection<Bookings> bookings) throws SQLException {
        source.preparedStatement("insert into bookings(book_ref, book_date, total_amount) values (?, ?, ?)", insertBookings -> {
            for (Bookings booking : bookings) {
                insertBookings.setString(1, booking.getBook_ref());
                insertBookings.setTimestamp(2, Timestamp.valueOf(booking.getBook_date().atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()));
                insertBookings.setDouble(3, booking.getTotal_amount());
                insertBookings.execute();
            }
        });
    }

    public int getBookingsCount() throws SQLException {
        return source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("select count(*) from bookings");
            resultSet.next();
            return resultSet.getInt(1);
        });
    }

    public Set<Bookings> getBookings() throws SQLException {
        return source.statement(stmt -> {
            Set<Bookings> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("select * from bookings");
            while (resultSet.next()) {
                result.add(createBookings(resultSet));
            }
            return result;
        });
    }
}
