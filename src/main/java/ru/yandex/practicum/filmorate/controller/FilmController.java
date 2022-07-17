package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

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
        return filmService.createFilm(film);
    }

    @Override
    @ResponseBody
    @PutMapping("/films")
    public Film update(@RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @Override
    @GetMapping("/films")
    public Collection<Film> getAll() {
        return filmService.getAllFilms();
    }

    @Override
    @ResponseBody
    @GetMapping("/films/{id}")
    public Film getObject(@PathVariable int id) {
        return filmService.getFilm(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void removeLike(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        filmService.removeLike(filmId, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getTopFilms(@RequestParam(value = "count", defaultValue = "10", required = false) int count) {
        return filmService.getTopFilms(count);
    }

}
