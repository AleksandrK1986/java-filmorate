package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import java.time.LocalDate;


@Data
public class Film {
    private static int counter=0;
    private int id = getNewId();
    private String name;
    private String description;
    private LocalDate releaseDate;
    private long duration; //minutes

    private int getNewId(){
        counter=counter+1;
        return counter;
    }
}
