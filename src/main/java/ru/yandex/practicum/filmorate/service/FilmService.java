package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.Validate;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    private final GenreDbStorage genreDbStorage;
    private final RatingDbStorage ratingDbStorage;
    private final LikeDbStorage likeDbStorage;

    @Autowired
    public FilmService( @Qualifier("FilmDbStorage") FilmStorage filmStorage,
                        @Qualifier("UserDbStorage")  UserStorage userStorage,
                        GenreDbStorage genreDbStorage,
                        RatingDbStorage ratingDbStorage,
                        LikeDbStorage likeDbStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreDbStorage = genreDbStorage;
        this.ratingDbStorage = ratingDbStorage;
        this.likeDbStorage = likeDbStorage;
    }

    public Film createFilm(Film film) {
        Validate.validateOrGetException(film);
        if(film.getMpa() == null){
            throw new ValidationException("В запросе отсутствует рейтинг MPA для фильма");
        }
        int idFilm = filmStorage.addFilmInStorage(film);
        log.info("Создание фильма {}      Фильм с ID {} создан успешно", LocalDateTime.now(), idFilm);
        for (Genre g: film.getGenres()){
            genreDbStorage.getGenreById(g.getId()); //проверка что такой тип жанра есть
            genreDbStorage.addFilmGenre(idFilm, g.getId());
            log.info("Создание фильма {}      К фильму с ID {} добавлен жанр {} успешно",
                    LocalDateTime.now(), idFilm, genreDbStorage.getGenreById(g.getId()).getName());
        }

        return getFilm(idFilm);
    }

    public Collection<Film> getAllFilms() {
        Collection<Film> films;
        films = filmStorage.getAllFilmsInStorage();
        for(Film f:films){
            Collection<Genre> genres = genreDbStorage.getAllFilmGenres(f.getId());
            Set<Genre> setGenres = new HashSet<Genre>(genres);
            f.setGenres(setGenres);
            log.info("Подготовка к получению списка фильмов {}      В фильм {} добавлены жанры успешно",
                    LocalDateTime.now(), f.getId());
        }
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
        genreDbStorage.removeFilmGenres(film.getId());
        for (Genre g: film.getGenres()){
            Genre newGenre = genreDbStorage.getGenreById(g.getId()); //проверка что такой тип жанра есть
                genreDbStorage.addFilmGenre(film.getId(), newGenre.getId());
                log.info("Обновление фильма {}      К фильму с ID {} добавлен жанр {} успешно",
                        LocalDateTime.now(), film.getId(), newGenre.getName());
        }
        return getFilm(film.getId());
    }

    public void addLike(int filmId, int userId) {
        Film film = filmStorage.getFilmInStorage(filmId);
        Like like = new Like(filmId, userId);
        userStorage.getUserInStorage(userId); // контроль что такой пользователь есть
        Set<Like> likes = new HashSet<Like>(likeDbStorage.getAllLikes(filmId));
        if(likes.contains(like)) {
            throw new ValidationException("Ошибка добавления лайка, такой лайк уже проставлен");
        }
        likeDbStorage.addLike(filmId, userId);
        film.setRate(film.getRate()+1);
        filmStorage.updateFilmInStorage(film);
        log.info("Лайк фильма {}      Пользователь с ID {} проставил лайк на фильм с ID {} успешно",
                LocalDateTime.now(),
                userId, filmId);
    }

    public void removeLike(int filmId, int userId) {
        Film film = filmStorage.getFilmInStorage(filmId);
        Like like = new Like(filmId, userId);
        userStorage.getUserInStorage(userId); // контроль что такой пользователь есть
        Set<Like> likes = new HashSet<Like>(likeDbStorage.getAllLikes(filmId));
        if(!likes.contains(like)) {
            throw new ValidationException("Ошибка удвления лайка, такой лайк не существует");
        }
        likeDbStorage.removeLike(filmId, userId);
        film.setRate(film.getRate()-1);
        filmStorage.updateFilmInStorage(film);
        log.info("Удаление лайка у фильма {}      Пользователь с ID {} удалил лайк на фильме с ID {} успешно",
                LocalDateTime.now(),
                userId, filmId);
    }

    public List<Film> getTopFilms(int count) {
        List<Film> filmsForReturn = filmStorage.getAllFilmsInStorage().stream()
                .filter(film -> film.getRate() != 0)
                .sorted(Comparator.comparing(film -> film.getRate(), Comparator.reverseOrder()))
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
        Collection<Genre> genres = genreDbStorage.getAllFilmGenres(id);
        Set<Genre> setGenres = new HashSet<Genre>(genres);
        film.setGenres(setGenres);
        log.info("Получение фильма {}      Фильм с ID {}, список жанров получен успешно", LocalDateTime.now(), id);
        return film;
    }

}
