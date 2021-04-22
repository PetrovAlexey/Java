package edu.phystech.jdbcdemo.service.dao;

import edu.phystech.jdbcdemo.domain.Tickets;
import edu.phystech.jdbcdemo.service.db.SimpleJdbcTemplate;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class TicketsDao {
    private final SimpleJdbcTemplate source;
    private Tickets createTickets(ResultSet resultSet) throws SQLException {
        return new Tickets(resultSet.getString("ticket_no"),
                resultSet.getString("book_ref"),
                resultSet.getString("passenger_id"),
                resultSet.getString("passenger_name"),
                resultSet.getString("contact_data"));
    }

    public void saveTickets(Collection<Tickets> tickets) throws SQLException {
        source.preparedStatement("insert into tickets(ticket_no, book_ref, passenger_id, passenger_name, contact_data) values (?, ?, ?, ?, ?)", insertTickets -> {
            for (Tickets ticket : tickets) {
                insertTickets.setString(1, ticket.getTicket_no());
                insertTickets.setString(2, ticket.getBook_ref());
                insertTickets.setString(3, ticket.getPassenger_id());
                insertTickets.setString(4, ticket.getPassenger_name());
                insertTickets.setString(5, ticket.getContact_data());
                insertTickets.execute();
            }
        });
    }

    public int getTicketsCount() throws SQLException {
        return source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("select count(*) from tickets");
            resultSet.next();
            return resultSet.getInt(1);
        });
    }

    public Set<Tickets> getTickets() throws SQLException {
        return source.statement(stmt -> {
            Set<Tickets> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("select * from tickets");
            while (resultSet.next()) {
                result.add(createTickets(resultSet));
            }
            return result;
        });
    }
}
