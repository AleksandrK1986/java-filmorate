package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.storage.rating.RatingDbStorage;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class RatingService {
    private final RatingDbStorage ratingDbStorage;

    @Autowired
    public RatingService(RatingDbStorage ratingDbStorage) {
        this.ratingDbStorage = ratingDbStorage;
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
