package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Component
@Qualifier("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int addFilmInStorage(Film film) {
        String sqlQuery = "insert into \"films\"(" +
                    "\"name\"," +
                    " \"description\"," +
                    " \"release_date\"," +
                    " \"duration\"," +
                    " \"rate\"," +
                    " \"rating_mpa\") values (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            stmt.setInt(5, film.getRate());
            stmt.setInt(6, film.getMpa().getId());
            return stmt;
            }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public void deleteFilmInStorage(int id) {
        String sqlQuery = "delete from \"films\" where \"id\" = ?";
        jdbcTemplate.update(sqlQuery, id);
    }
    @Override
    public void updateFilmInStorage(Film film) {
        String sqlQuery = "update \"films\" set" +
                "\"name\" = ?," +
                " \"description\"= ?," +
                " \"release_date\"= ?," +
                " \"duration\"= ?," +
                " \"rate\"= ?," +
                " \"rating_mpa\" = ? where \"id\" = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId());
    }

    @Override
    public Collection<Film> getAllFilmsInStorage() {
        String sql = "select * from \"films\"";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film getFilmInStorage(int id) {
        String sql = "select * from \"films\" where \"id\" = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), id).get(0);
    }
    private Film makeFilm(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        long duration = rs.getLong("duration");
        int rate = rs.getInt("rate");
        int mpaId = rs.getInt("rating_mpa");
        RatingMPA mpa = new RatingMPA(mpaId);
        return new Film(id, name, description, releaseDate, duration, rate, mpa, null);
    }
}
