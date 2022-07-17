package ru.yandex.practicum.filmorate.storage.relationship;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Relationship;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
public class RelationshipDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public RelationshipDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addFriend (int userId, int friendId) {
        String sqlQuery = "insert into \"relationship\"(\"user_id\", \"friend_id\", \"relationship_status\") " +
                "values (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId, true);
    }

    public void removeFriend (int userId, int friendId) {
        String sqlQuery = "delete from \"relationship\" where \"user_id\" = ? AND \"friend_id\" = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    public Collection<Relationship> getAllFriends (int userId) {
        String sql = "select * from \"relationship\" where \"user_id\" = ? AND \"relationship_status\" = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeRelationship(rs), userId, true);
    }

    public Collection<Integer> getCommonFriends (int userId, int otherUserId) {
        String sql = "SELECT" +
                "\"r\".\"friend_id\"" +
                "FROM \"relationship\" AS \"r\"" +
                "JOIN \"relationship\" AS \"rr\" ON \"r\".\"friend_id\" = \"rr\".\"friend_id\"" +
                "WHERE \"r\".\"user_id\" = ?" +
                "AND \"rr\".\"user_id\" = ?" +
                "AND \"r\".\"relationship_status\" = true";
        return jdbcTemplate.queryForList(sql, Integer.class, userId, otherUserId);
    }
    private Relationship makeRelationship(ResultSet rs) throws SQLException {
        int userId = rs.getInt("user_id");
        int friendId = rs.getInt("friend_id");
        boolean status = rs.getBoolean("relationship_status");
        return new Relationship(userId, friendId, status);
    }
}
