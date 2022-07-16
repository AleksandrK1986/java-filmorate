package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikeDbStorage;
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
            //film.setMpa(new RatingMPA(1));
            throw new ValidationException("В запросе отсутствует рейтинг MPA для фильма");
        }
        int idFilm = filmStorage.addFilmInStorage(film);
        log.info("Создание фильма {}      Фильм с ID {} создан успешно", LocalDateTime.now(), idFilm);
        for (Genre g: film.getGenres()){
            genreDbStorage.getGenreById(g.getId()); //проверка что такой тип жанра есть
            genreDbStorage.addFilmGenre(idFilm, g.getId());
            log.info("Создание фильма {}      К фильму с ID {} добавлен жанр {} успешно",
                    LocalDateTime.now(), idFilm, g.getName());
        }
        return getFilm(idFilm);
    }

    public Collection<Film> getAllFilms() {
        Collection<Film> films;
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
        genreDbStorage.removeFilmGenres(film.getId());
        for (Genre g: film.getGenres()){
            genreDbStorage.getGenreById(g.getId()); //проверка что такой тип жанра есть
                genreDbStorage.addFilmGenre(film.getId(), g.getId());
                log.info("Обновление фильма {}      К фильму с ID {} добавлен жанр {} успешно",
                        LocalDateTime.now(), film.getId(), g.getName());
        }
        return getFilm(film.getId());
    }

    public void addLike(int filmId, int userId) {
        if(filmId <= 0 || userId <= 0){
            throw new NoSuchElementException("Фильм или пользователь по переданному ID не найден в БД");
        }
        Film film = filmStorage.getFilmInStorage(filmId);
        Like like = new Like(filmId, userId);
        userStorage.getUserInStorage(userId); // контроль что такой пользователь есть
        //film.addLike(userId);
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
        if(filmId <= 0 || userId <= 0){
            throw new NoSuchElementException("Фильм или пользователь по переданному ID не найден в БД");
        }
        Film film = filmStorage.getFilmInStorage(filmId);
        Like like = new Like(filmId, userId);
        userStorage.getUserInStorage(userId); // контроль что такой пользователь есть
        //film.removeLike(userId);
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
                //.filter(film -> film.getLikes() != null)
                .filter(film -> film.getRate() != 0)
                //.sorted(Comparator.comparing(film -> film.getLikes().size(), Comparator.reverseOrder()))
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
        if(id<=0){
            throw new NoSuchElementException("Фильм с таким ID " + id + " не найден");
        }
        Film film = filmStorage.getFilmInStorage(id);
        log.info("Получение фильма {}      Фильм с ID {} получен успешно", LocalDateTime.now(), id);
        Collection<Genre> genres = genreDbStorage.getAllFilmGenres(id);
        Set<Genre> setGenres = new HashSet<Genre>(genres);
        film.setGenres(setGenres);
        log.info("Получение фильма {}      Фильм с ID {}, список жанров получен успешно", LocalDateTime.now(), id);
        return film;
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

    public Collection<RatingMPA> getAllMPA() {
        log.info("Получение списка рейтингов {}      Рейтинги получены успешно",
                LocalDateTime.now());
        return ratingDbStorage.getAllMPA();
    }

    public RatingMPA getMPAById(int id) {
        if(id<=0){
            throw new NoSuchElementException("Рейтинг с таким ID " + id + " не найден");
        }
        log.info("Получение рейтинга по ID {}      Рейтинг c ID {} получен успешно",
                LocalDateTime.now(), id);
        return ratingDbStorage.getMPAById(id);
    }
}
