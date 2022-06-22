package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
public class FilmController extends Controller<Film> {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @Override
    @ResponseBody
    @PostMapping("/films")
    public Film create(@RequestBody Film film) {
        int idFilm = filmService.createFilm(film);
        log.info("Создание фильма {}      Фильм с ID {} создан успешно", LocalDateTime.now(), film.toString());
        return filmService.getFilm(idFilm);
    }

    @Override
    @ResponseBody
    @PutMapping("/films")
    public Film update(@RequestBody Film film) {
        filmService.updateFilm(film);
        log.info("Обновление фильма {}      Фильм с ID {} обновлен успешно", LocalDateTime.now(), film.toString());
        return filmService.getFilm(film.getId());
    }

    @Override
    @GetMapping("/films")
    public List<Film> getAll() {
        return filmService.getAllFilms();
    }

    @Override
    @ResponseBody
    @GetMapping("/films/{id}")
    public Film getObject(@PathVariable int id) {
        Film film = filmService.getFilm(id);
        log.info("Получение фильма {}      Фильм с ID {} получен успешно", LocalDateTime.now(), id);
        return film;
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        filmService.addLike(filmId, userId);
        log.info("Лайк фильма {}      Пользователь с ID {} проставил лайк на фильм с ID {} успешно",
                LocalDateTime.now(),
                userId, filmId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void removeLike(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        filmService.removeLike(filmId, userId);
        log.info("Удаление лайка у фильма {}      Пользователь с ID {} удалил лайк на фильме с ID {} успешно",
                LocalDateTime.now(),
                userId, filmId);
    }

    @GetMapping("/films/popular")
    public List<Film> getTopFilms(@RequestParam(value = "count", defaultValue = "10", required = false) int count) {
        List<Film> topFilms = filmService.getTopFilms(count);
        if (topFilms != null) {
            log.info("Получение популярных фильмов {}      Получение списка из самых популярных {} " +
                            "фильмов выполнено успешно", LocalDateTime.now(),
                    count);
        } else {
            log.info("Получение популярных фильмов {}      список фильмов пуст", LocalDateTime.now());
        }
        return topFilms;
    }
}
