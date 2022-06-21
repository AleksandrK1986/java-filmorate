package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void createFilm(Film film) {
        filmStorage.addFilmInStorage(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilmsInStorage();
    }

    public void updateFilm(Film film) {
        filmStorage.updateFilmInStorage(film);
    }

    public void addLike(int filmId, int userId) {
        Film film = filmStorage.getFilmInStorage(filmId);
        userStorage.getUserInStorage(userId); // контроль что такой пользователь есть
        film.addLike(userId);
        filmStorage.updateFilmInStorage(film);
    }

    public void removeLike(int filmId, int userId) {
        Film film = filmStorage.getFilmInStorage(filmId);
        userStorage.getUserInStorage(userId); // контроль что такой пользователь есть
        film.removeLike(userId);
        filmStorage.updateFilmInStorage(film);
    }

    public List<Film> getTopFilms() {
        List<Film> filmsForReturn = filmStorage.getAllFilmsInStorage().stream()
                .filter(film -> film.getLikes() != null)
                .sorted(Comparator.comparing(film -> film.getLikes().size(), Comparator.reverseOrder()))
                .collect(Collectors.toList());
        return filmsForReturn;
    }

    public Film getFilm(int id) {
        return filmStorage.getFilmInStorage(id);
    }
}