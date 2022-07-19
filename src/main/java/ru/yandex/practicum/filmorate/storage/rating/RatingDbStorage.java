package ru.yandex.practicum.filmorate.storage.rating;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
public class RatingDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public RatingDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public Collection<RatingMPA> getAllMPA() {
        String sql = "select * from \"rating_mpa\"";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeRating(rs));
    }

    public RatingMPA getMPAById(int id) {
        String sql = "select * from \"rating_mpa\" where \"id\" = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeRating(rs), id).get(0);
    }

    private RatingMPA makeRating(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        return new RatingMPA(id, name);
    }
}
