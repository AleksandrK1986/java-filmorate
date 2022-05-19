package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.validation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class FilmController {
    private final List<Film> films = new ArrayList<>();

    @ResponseBody
    @PostMapping(value = "/films")
    public Film create(@RequestBody Film film, HttpServletResponse response) {
        try{
            Validate.validateOrGetException(film, response);
            films.add(film);
            log.info("Создание фильма {}      Фильм с ID {} создан успешно", LocalDateTime.now(),
                    film.getId());
        } catch (ValidationException e) {
            log.info("Ошибка создания фильма {}    Не выполняется условие: {}", LocalDateTime.now(), e.getMessage());
        }
        return film;
    }

    @ResponseBody
    @PutMapping(value = "/films")
    public Film update(@RequestBody Film film, HttpServletResponse response) {
        try{
            Validate.validateOrGetException(film, response);
            Film oldFilm = new Film();
            for(Film f: films){
                if(f.getId() == film.getId()){
                    oldFilm = f;
                }
            }
            films.remove(oldFilm);
            films.add(film);
            log.info("Обновление фильма {}      Фильм с ID {} обновлен успешно", LocalDateTime.now(),
                    film.getId());
        } catch (ValidationException e) {
            log.info("Ошибка обновления фильма {}    Не выполняется условие: {}", LocalDateTime.now(), e.getMessage());
        }
        return film;
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        return films;
    }
}
