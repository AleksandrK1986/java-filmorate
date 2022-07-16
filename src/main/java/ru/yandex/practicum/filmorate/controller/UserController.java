package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@RestController
public class UserController extends Controller<User> {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    @ResponseBody
    @PostMapping("/users")
    public User create(@RequestBody User user) {
        return userService.createUser(user);
    }

    @Override
    @ResponseBody
    @PutMapping("/users")
    public User update(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @Override
    @GetMapping("/users")
    public Collection<User> getAll() {
        return userService.getAllUsers();
    }

    @Override
    @ResponseBody
    @GetMapping("/users/{id}")
    public User getObject(@PathVariable int id) {
        return userService.getUser(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") int userId, @PathVariable("friendId") int friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable("id") int userId, @PathVariable("friendId") int friendId) {
        userService.removeFriend(userId, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getAllFriends(@PathVariable("id") int userId) {
        return userService.getAllFriends(userId);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") int userId, @PathVariable("otherId") int otherId) {
        return userService.getCommonFriends(userId, otherId);
    }

}

