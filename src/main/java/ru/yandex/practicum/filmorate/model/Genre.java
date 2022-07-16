package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
    @NonNull
    int id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return id == genre.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getName(){
        String name = null;
        switch (this.id){
            case 1: name = "Комедия"; break;
            case 2: name = "Драма"; break;
            case 3: name = "Мультфильм"; break;
            case 4: name = "Триллер"; break;
            case 5: name = "Документальный"; break;
            case 6: name = "Боевик"; break;
        }
        return name;
    }
}