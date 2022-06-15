package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.validation.Validate;
import ru.yandex.practicum.filmorate.validation.ValidationException;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class Controller <T> {
    private final List<T> objects = new ArrayList<>();

    protected T create (T object, HttpServletResponse response){
        try{
            Validate.validateOrGetException(object, response);
            addObject(object);
        } catch (ValidationException e) {
            log.info("Ошибка создания объекта {}    Не выполняется условие: {}", LocalDateTime.now(), e.getMessage());
        }
        return object;
    }

    protected T update(T object, HttpServletResponse response) {
        try{
            Validate.validateOrGetException(object, response);
            T oldObject = (T) new Object();
            for(T o: objects){
                if(o.equals(object)){
                    oldObject = o;
                }
            }
            objects.remove(oldObject);
            addObject(object);
        } catch (ValidationException e) {
            log.info("Ошибка обновления объекта {}    Не выполняется условие: {}", LocalDateTime.now(), e.getMessage());
        }
        return object;
    }

    protected List<T> getAll() {
        return objects;
    }

    private void addObject(T object) {
        objects.add(object);
        log.info("Создание объекта {}      Объект с ID {} создан успешно", LocalDateTime.now(),
                object.toString());
    }
}
