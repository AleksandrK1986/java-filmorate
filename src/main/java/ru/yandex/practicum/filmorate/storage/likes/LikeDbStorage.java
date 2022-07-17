package ru.yandex.practicum.filmorate.storage.likes;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Like;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
public class LikeDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    public void addLike(int filmId, int userId) {
        String sqlQuery = "insert into \"likes\" values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        String sqlQuery = "delete from \"likes\" where \"film_id\" = ? AND \"user_id\" = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    public Collection<Like> getAllLikes(int filmId) {
        String sql = "select * from \"likes\" where \"film_id\" = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeLike(rs), filmId);
    }

    private Like makeLike(ResultSet rs) throws SQLException {
        int filmId = rs.getInt("film_id");
        int userId = rs.getInt("user_id");
        return new Like(filmId, userId);
    }
}
