package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private HashMap<Integer, Film> filmsInStorage = new HashMap<>();
    private static int counter = 0;

    @Override
    public int addFilmInStorage(Film film) {
        film.setId(getNewId());
        filmsInStorage.put(film.getId(), film);
        return film.getId();
    }

    @Override
    public void deleteFilmInStorage(int id) {
        if (filmsInStorage.containsKey(id)) {
            filmsInStorage.remove(id);
        } else {
            throw new NoSuchElementException("Фильм с ID " + id + " не найден в хранилище");
        }
    }

    @Override
    public void updateFilmInStorage(Film film) {
        if (filmsInStorage.containsKey(film.getId())) {
            filmsInStorage.remove(film.getId());
            filmsInStorage.put(film.getId(), film);
        } else {
            throw new NoSuchElementException("Фильм с ID " + film.getId() + " не найден в хранилище");
        }
    }

    @Override
    public List<Film> getAllFilmsInStorage() {
        List<Film> films = new ArrayList<>();
        for (Film f : filmsInStorage.values()) {
            films.add(f);
        }
        return films;
    }

    @Override
    public Film getFilmInStorage(int id) {
        if (filmsInStorage.containsKey(id)) {
            return filmsInStorage.get(id);
        } else {
            throw new NoSuchElementException("Фильм с ID " + id + " не найден в хранилище");
        }
    }

    private int getNewId() {
        counter = counter + 1;
        return counter;
    }
}
