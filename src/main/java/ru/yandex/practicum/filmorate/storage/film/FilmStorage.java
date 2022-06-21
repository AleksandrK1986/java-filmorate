package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    void addFilmInStorage(Film film);

    void deleteFilmInStorage(int id);

    void updateFilmInStorage(Film film);

    List<Film> getAllFilmsInStorage();

    Film getFilmInStorage(int id);

}
