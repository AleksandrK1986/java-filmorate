package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class FilmController extends Controller<Film>{

    @ResponseBody
    @PostMapping("/films")
    public Film createFilm(@RequestBody Film film, HttpServletResponse response) {
        return create(film, response);
    }

    @ResponseBody
    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film, HttpServletResponse response) {
        return update(film, response);
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return getAll();
    }

}
