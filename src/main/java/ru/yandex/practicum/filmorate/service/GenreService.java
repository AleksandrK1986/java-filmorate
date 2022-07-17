package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class GenreService {
    private final GenreDbStorage genreDbStorage;

    @Autowired
    public GenreService(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    public Collection<Genre> getAllGenres() {
        log.info("Получение списка жанров {}      Жанры получены успешно",
                LocalDateTime.now());
        return genreDbStorage.getAllGenres();
    }

    public Genre getGenreById(int id) {
        if(id<=0){
            throw new NoSuchElementException("Жанр с таким ID " + id + " не найден");
        }
        log.info("Получение жанра по ID {}      Жанр c ID {} получен успешно",
                LocalDateTime.now(), id);
        return genreDbStorage.getGenreById(id);
    }

}
