package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    int addFilmInStorage(Film film);

    void deleteFilmInStorage(int id);

    void updateFilmInStorage(Film film);

    Collection<Film> getAllFilmsInStorage();

    Film getFilmInStorage(int id);

}
