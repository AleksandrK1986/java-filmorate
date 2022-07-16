package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.NoSuchElementException;

@Component
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    public void addFilmGenre (int filmId, int genreId) {
        String sqlQuery = "insert into \"film_genre\" values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, genreId);
    }
    public void removeFilmGenres (int filmId) {
        String sqlQuery = "delete from \"film_genre\" where \"film_id\" = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    public Collection<Genre> getAllGenres () {
        String sql = "select * from \"genre_type\"";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    public Collection<Genre> getAllFilmGenres (int filmId) {
        String sql = "select * from \"film_genre\" join \"genre_type\" " +
                "on \"film_genre\".\"genre_id\" = \"genre_type\".\"id\" where \"film_id\" = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), filmId);
    }

    public Genre getGenreById (int genreId) {
        String sql = "select * from \"genre_type\" where \"id\" = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), genreId).get(0);
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        return new Genre(id);
    }

}
