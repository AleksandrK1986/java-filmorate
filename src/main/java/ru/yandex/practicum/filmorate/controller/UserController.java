package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class UserController extends Controller<User> {

    @ResponseBody
    @PostMapping("/users")
    public User createUser(@RequestBody User user, HttpServletResponse response) {
        return update(ifEmptyNameRename(user), response);
    }

    @ResponseBody
    @PutMapping("/users")
    public User updateUser(@RequestBody User user, HttpServletResponse response) {
        return update(ifEmptyNameRename(user), response);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return getAll();
    }

    private User ifEmptyNameRename(User user) {
        if(user.getName().isBlank()){
            user.setName(user.getLogin());
        }
        return user;
    }
}
