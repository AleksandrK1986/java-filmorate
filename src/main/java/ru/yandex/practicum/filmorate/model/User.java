package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class User {
    private static int counter=0;
    private int id = getNewId();
    private String email;
    private String login;
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