package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.Validate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film createFilm(Film film) {
        Validate.validateOrGetException(film);
        int idFilm = filmStorage.addFilmInStorage(film);
        log.info("Создание фильма {}      Фильм с ID {} создан успешно", LocalDateTime.now(), idFilm);
        return getFilm(idFilm);
    }

    public List<Film> getAllFilms() {
        List<Film> films;
        films = filmStorage.getAllFilmsInStorage();
        if (films != null) {
            log.info("Получение списка фильмов {}      Фильмы получены успешно", LocalDateTime.now());
        } else {
            log.info("Получение списка фильмов {}      Фильмы отсутствуют", LocalDateTime.now());
        }
        return films;
    }

    public Film updateFilm(Film film) {
        Validate.validateOrGetException(film);
        filmStorage.updateFilmInStorage(film);
        log.info("Обновление фильма {}      Фильм с ID {} обновлен успешно", LocalDateTime.now(), film.getId());
        return filmStorage.getFilmInStorage(film.getId());
    }

    public void addLike(int filmId, int userId) {
        Film film = filmStorage.getFilmInStorage(filmId);
        userStorage.getUserInStorage(userId); // контроль что такой пользователь есть
        film.addLike(userId);
        filmStorage.updateFilmInStorage(film);
        log.info("Лайк фильма {}      Пользователь с ID {} проставил лайк на фильм с ID {} успешно",
                LocalDateTime.now(),
                userId, filmId);
    }

    public void removeLike(int filmId, int userId) {
        Film film = filmStorage.getFilmInStorage(filmId);
        userStorage.getUserInStorage(userId); // контроль что такой пользователь есть
        film.removeLike(userId);
        filmStorage.updateFilmInStorage(film);
        log.info("Удаление лайка у фильма {}      Пользователь с ID {} удалил лайк на фильме с ID {} успешно",
                LocalDateTime.now(),
                userId, filmId);
    }

    public List<Film> getTopFilms(int count) {
        List<Film> filmsForReturn = filmStorage.getAllFilmsInStorage().stream()
                .filter(film -> film.getLikes() != null)
                .sorted(Comparator.comparing(film -> film.getLikes().size(), Comparator.reverseOrder()))
                .limit(count)
                .collect(Collectors.toList());
        if (filmsForReturn != null) {
            log.info("Получение популярных фильмов {}      Получение списка из самых популярных {} " +
                            "фильмов выполнено успешно", LocalDateTime.now(),
                    count);
        } else {
            log.info("Получение популярных фильмов {}      список фильмов пуст", LocalDateTime.now());
        }
        return filmsForReturn;
    }

    public Film getFilm(int id) {
        Film film = filmStorage.getFilmInStorage(id);
        log.info("Получение фильма {}      Фильм с ID {} получен успешно", LocalDateTime.now(), id);
        return film;
    }

}
