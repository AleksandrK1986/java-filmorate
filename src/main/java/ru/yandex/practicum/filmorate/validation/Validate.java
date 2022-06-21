package ru.yandex.practicum.filmorate.validation;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.NoSuchElementException;

public class Validate {
    public static void validateOrGetException(Object o) throws ValidationException {
        if (o.getClass() == Film.class) {
            if (((Film) o).getId() <= 0) {
                throw new NoSuchElementException("Некорректный id фильма");
            }
            if (((Film) o).getName().isBlank()) {
                throw new ValidationException("Название фильма не может быть пустым");
            }
            if (((Film) o).getDescription().length() > 200) {
                throw new ValidationException("Максимальная длина описания фильма — 200 символов");
            }
            if (((Film) o).getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                throw new ValidationException("Дата релиза фильма — не раньше 28 декабря 1895 года");
            }
            if (((Film) o).getDuration() <= 0) {
                throw new ValidationException("Продолжительность фильма должна быть положительной");
            }
        } else if (o.getClass() == User.class) {
            if (((User) o).getId() <= 0) {
                throw new NoSuchElementException("Некорректный id пользователя");
            }
            if (!((User) o).getEmail().contains("@") || ((User) o).getEmail().isBlank()) {
                throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
            }
            if (((User) o).getLogin().isEmpty() || ((User) o).getLogin().contains(" ")) {
                throw new ValidationException("Логин не может быть пустым и содержать пробелы");
            }
            if (((User) o).getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("Дата рождения не может быть в будущем");
            }

        } else {
            throw new ValidationException("Передан неизвестный объект, проверка не пройдена");
        }
    }

}
