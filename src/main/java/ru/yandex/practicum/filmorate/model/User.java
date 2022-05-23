package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private static int counter=0;
    @Min(1)
    private int id = getNewId();
    @Email
    private String email;
    @NotBlank
    private String login;
    @Size(min = 0, max = 100)
    private String name;
    private LocalDate birthday;

    private int getNewId(){
        counter=counter+1;
        return counter;
    }

}
/*
Свойства model.User:
целочисленный идентификатор — id;
электронная почта — email;
логин пользователя — login;
имя для отображения — name;
дата рождения — birthday.
 */