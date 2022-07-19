package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Relationship {
    int userId;
    int friendId;
    boolean status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Relationship that = (Relationship) o;
        return userId == that.userId && friendId == that.friendId && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, friendId, status);
    }
}
