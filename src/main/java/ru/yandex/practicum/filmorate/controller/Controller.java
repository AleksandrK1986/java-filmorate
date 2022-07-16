package ru.yandex.practicum.filmorate.controller;

import java.util.Collection;
import java.util.List;

public abstract class Controller<T> {

    abstract T create(T object);

    abstract T update(T object);

    abstract Collection<T> getAll();

    abstract T getObject(int id);

}
