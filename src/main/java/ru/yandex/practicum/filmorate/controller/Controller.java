package ru.yandex.practicum.filmorate.controller;

import java.util.List;

public abstract class Controller<T> {

    abstract T create(T object);

    abstract T update(T object);

    abstract List<T> getAll();

    abstract T getObject(int id);

}
