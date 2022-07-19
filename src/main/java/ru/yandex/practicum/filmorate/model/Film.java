package ru.yandex.practicum.filmorate.model;

import lombok.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    private static int counter=0;
    @Min(1)
    private int id = getNewId();
    @NotBlank
    @Size(min = 1, max = 100)
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    private LocalDate releaseDate;
    @Min(1)
    private long duration; //minutes
    private int rate;
    private RatingMPA mpa;
    private Set<Genre> genres = new HashSet<>();

    private int getNewId(){
        counter=counter+1;
        return counter;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Film)) return false;
        Film film = (Film) o;
        return id == film.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

