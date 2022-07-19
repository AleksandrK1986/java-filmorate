package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
public class RatingMPA {
    @NonNull
    int id;
    String name;

    public RatingMPA(@NonNull int id, String name) {
        this.id = id;
        this.name = name;
    }

    public RatingMPA(@NonNull int id) {
        this.id = id;
    }
}


