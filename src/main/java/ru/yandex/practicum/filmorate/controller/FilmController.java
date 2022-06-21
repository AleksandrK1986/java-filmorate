package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class FilmController extends Controller<Film> {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @ResponseBody
    @PostMapping("/films")
    public Film createFilm(@RequestBody Film film) {
        return create(film);
    }

    @ResponseBody
    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        return update(film);
    }

    @Override
    @GetMapping("/films")
    public List<Film> getAll() {
        return filmService.getAllFilms();
    }

    @Override
    void saveObject(Film film) {
        filmService.createFilm(film);
    }

    @Override
    void updateObject(Film film) {
        filmService.updateFilm(film);
    }

    @Override
    @ResponseBody
    @GetMapping("/films/{id}")
    public Film getObject(@PathVariable int id) {
        Film film = null;
        film = filmService.getFilm(id);
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
        List<Film> topFilms = new ArrayList<>();
        List<Film> films = filmService.getTopFilms();
        for (int i = 0; i < count && i < films.size(); i++) {
            topFilms.add(films.get(i));
        }
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
