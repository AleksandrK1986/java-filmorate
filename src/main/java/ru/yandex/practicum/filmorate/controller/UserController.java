package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class UserController extends Controller<User> {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseBody
    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        return create(ifEmptyNameRename(user));
    }

    @ResponseBody
    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        return update(ifEmptyNameRename(user));
    }

    @Override
    @GetMapping("/users")
    public List<User> getAll() {
        List<User> users = null;
        users = userService.getAllUsers();

        if (users != null) {
            log.info("Получение списка пользователей {}      Пользователи получены успешно", LocalDateTime.now());
        } else {
            log.info("Получение списка пользователей {}      Пользователи отсутствуют", LocalDateTime.now());
        }
        return users;
    }

    private User ifEmptyNameRename(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return user;
    }

    @Override
    void saveObject(User user) {
        userService.createUser(user);
    }

    @Override
    void updateObject(User user) {
        userService.updateUser(user);
    }

    @Override
    @ResponseBody
    @GetMapping("/users/{id}")
    public User getObject(@PathVariable int id) {
        User user = null;
        user = userService.getUser(id);
        log.info("Получение пользователя {}      Пользователь с ID {} получен успешно", LocalDateTime.now(), id);
        return user;
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") int userId, @PathVariable("friendId") int friendId) {
        userService.addFriend(userId, friendId);
        log.info("Добавление в друзья {}      Пользователь с ID {} добавил друга с ID {} успешно",
                LocalDateTime.now(),
                userId, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable("id") int userId, @PathVariable("friendId") int friendId) {
        userService.removeFriend(userId, friendId);
        log.info("Удаление из друзей {}      Пользователь с ID {} удалил друга с ID {} успешно",
                LocalDateTime.now(),
                userId, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getAllFriends(@PathVariable("id") int userId) {
        List<User> users = new ArrayList<>();
        users = userService.getAllFriends(userId);
        log.info("Получение списка друзей {}    пользователя с ID {} выполнено успешно",
                LocalDateTime.now(),
                userId);
        return users;
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getAllFriends(@PathVariable("id") int userId, @PathVariable("otherId") int otherId) {
        List<User> users = new ArrayList<>();
        users = userService.getCommonFriends(userId, otherId);
        if (users == null) {
            log.info("Получение списка общих друзей {}    пользователи с ID {} и с ID {} не имеют общих друзей",
                    LocalDateTime.now(),
                    userId, otherId);
        } else {
            log.info("Получение списка общих друзей {}    пользователя с ID {} и с ID {} выполнено успешно",
                    LocalDateTime.now(),
                    userId, otherId);
        }
        return users;
    }

}

