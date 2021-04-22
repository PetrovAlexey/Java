package edu.phystech.jdbcdemo.service.dao;

import edu.phystech.jdbcdemo.domain.BoardingPasses;
import edu.phystech.jdbcdemo.service.db.SimpleJdbcTemplate;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class BoardingPassesDao {
    private final SimpleJdbcTemplate source;
    private BoardingPasses createBoardingPasses(ResultSet resultSet) throws SQLException {
        return new BoardingPasses(resultSet.getString("ticket_no"),
                resultSet.getInt("flight_id"),
                resultSet.getInt("boarding_no"),
                resultSet.getString("seat_no"));
    }

    public void saveBoardingPasses(Collection<BoardingPasses> boardingPasses) throws SQLException {
        source.preparedStatement("insert into boarding_passes(ticket_no, flight_id, boarding_no, seat_no) values (?, ?, ?, ?)", insertBoardingPass -> {
            for (BoardingPasses boardingPass : boardingPasses) {
                insertBoardingPass.setString(1, boardingPass.getTicket_no());
                insertBoardingPass.setInt(2, boardingPass.getFlight_id());
                insertBoardingPass.setInt(3, boardingPass.getBoarding_no());
                insertBoardingPass.setString(4, boardingPass.getSeat_no());
                insertBoardingPass.execute();
            }
        });
    }

    public int getBoardingPassesCount() throws SQLException {
        return source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("select count(*) from boarding_passes");
            resultSet.next();
            return resultSet.getInt(1);
        });
    }

    public Set<BoardingPasses> getBoardingPasses() throws SQLException {
        return source.statement(stmt -> {
            Set<BoardingPasses> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("select * from boarding_passes");
            while (resultSet.next()) {
                result.add(createBoardingPasses(resultSet));
            }
            return result;
        });
    }
}
