package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Like {
  @NonNull
  int filmId;
  @NonNull
  int userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Like like = (Like) o;
        return filmId == like.filmId && userId == like.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(filmId, userId);
    }
}
