package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.validation.Validate;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public abstract class Controller<T> {

    protected T create(T object) {
        Validate.validateOrGetException(object);
        saveObject(object);
        log.info("Создание объекта {}      Объект с ID {} создан успешно", LocalDateTime.now(), object.toString());
        return object;
    }

    protected T update(T object) {
        Validate.validateOrGetException(object);
        updateObject(object);
        log.info("Обновление объекта {}      Объект с ID {} обновлен успешно", LocalDateTime.now(), object.toString());
        return object;
    }

    abstract void saveObject(T object);

    abstract void updateObject(T object);

    abstract List<T> getAll();

    abstract T getObject(int id);

}
