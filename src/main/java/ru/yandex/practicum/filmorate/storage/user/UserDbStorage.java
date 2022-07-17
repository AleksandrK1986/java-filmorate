package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;

@Component
@Qualifier("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int addUserInStorage(User user) {
        String sqlQuery = "insert into \"users\"(" +
                "\"email\"," +
                " \"login\"," +
                " \"name\"," +
                " \"birthday\") values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public void deleteUserInStorage(int id) {
        String sqlQuery = "delete from \"users\" where \"id\" = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    public void updateUserInStorage(User user) {
        String sqlQuery = "update \"users\" set" +
                "\"email\" = ?," +
                " \"login\"= ?," +
                " \"name\"= ?," +
                " \"birthday\"= ? where \"id\" = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getId());
    }

    public Collection<User> getAllUsersInStorage() {
        String sql = "select * from \"users\"";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    public User getUserInStorage(int id) {
        String sql = "select * from \"users\" where \"id\" = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id).get(0);
    }

    private User makeUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        return new User(id, email, login, name, birthday);
    }

}

