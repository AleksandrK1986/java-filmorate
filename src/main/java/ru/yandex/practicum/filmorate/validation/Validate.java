package ru.yandex.practicum.filmorate.validation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.LocalDate;

public class Validate {
    public static void validateOrGetException(Object o, HttpServletResponse response) throws ValidationException {
        if(o.getClass() == Film.class) {
            if(((Film) o).getId()<=0) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                throw new ValidationException("Некорректный id фильма");
            }
            if(((Film) o).getName().isBlank()){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                throw new ValidationException("Название фильма не может быть пустым");
            }
            if(((Film) o).getDescription().length()>200) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                throw new ValidationException("Максимальная длина описания фильма — 200 символов");
            }
            if(((Film) o).getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                throw new ValidationException("Дата релиза фильма — не раньше 28 декабря 1895 года");
            }
            if(((Film) o).getDuration()<=0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                throw new ValidationException("Продолжительность фильма должна быть положительной");
            }
        } else if(o.getClass() == User.class) {
            if(((User) o).getId()<=0) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                throw new ValidationException("Некорректный id пользователя");
            }
            if(!((User) o).getEmail().contains("@") || ((User) o).getEmail().isBlank()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
            }
            if(((User) o).getLogin().isEmpty() || ((User) o).getLogin().contains(" ")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                throw new ValidationException("Логин не может быть пустым и содержать пробелы");
            }
            if(((User) o).getBirthday().isAfter(LocalDate.now())) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                throw new ValidationException("Дата рождения не может быть в будущем");
            }

        } else {
            throw new ValidationException("Передан неизвестный объект, проверка не пройдена");
        }
    }
}

/*
Для Film:
название не может быть пустым;
максимальная длина описания — 200 символов;
дата релиза — не раньше 28 декабря 1895 года;
продолжительность фильма должна быть положительной.

Для User:
электронная почта не может быть пустой и должна содержать символ @;
логин не может быть пустым и содержать пробелы;
имя для отображения может быть пустым — в таком случае будет использован логин;
дата рождения не может быть в будущем
 */