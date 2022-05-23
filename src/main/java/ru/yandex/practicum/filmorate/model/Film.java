package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

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

    private int getNewId(){
        counter=counter+1;
        return counter;
    }
}

