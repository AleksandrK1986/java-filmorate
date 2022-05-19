package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.Validate;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class UserController {
    private List<User> users = new ArrayList<>();

    @ResponseBody
    @PostMapping(value = "/users")
    public User create(@RequestBody User user, HttpServletResponse response) {

        try{
            Validate.validateOrGetException(user, response);
            if(user.getName().isBlank()){
                user.setName(user.getLogin());
            }
            users.add(user);
            log.info("Создание пользователя {}      Пользователь с ID {} создан успешно", LocalDateTime.now(), user.getId());
        } catch (ValidationException e) {
            log.info("Ошибка создания пользователя {}    Не выполняется условие: {}", LocalDateTime.now(),
                    e.getMessage());
        }

        return user;
    }

    @ResponseBody
    @PutMapping(value = "/users")
    public User update(@RequestBody User user, HttpServletResponse response) {
        try{
            Validate.validateOrGetException(user, response);
            User oldUser = new User();
            if(user.getName().isBlank()){
                user.setName(user.getLogin());
            }
            for(User u: users){
                if(u.getId() == user.getId()){
                    oldUser = u;
                }
            }
            users.remove(oldUser);
            users.add(user);
            log.info("Обновление пользователя {}      Пользователь с ID {} обновлен успешно", LocalDateTime.now(),
                    user.getId());
        } catch (ValidationException e) {
            log.info("Ошибка обновления пользователя {}    Не выполняется условие: {}", LocalDateTime.now(),
                    e.getMessage());
        }
        return user;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return users;
    }

}
