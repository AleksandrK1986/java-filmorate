package ru.yandex.practicum.filmorate.controller;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public abstract class Controller <T> {
    public abstract T create(T object, HttpServletResponse response);
    public abstract T update(T object, HttpServletResponse response);
    public abstract List<T> getAll();
}
