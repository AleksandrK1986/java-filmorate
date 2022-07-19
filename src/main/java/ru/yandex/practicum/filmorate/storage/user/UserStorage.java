package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {
    int addUserInStorage(User user);

    void deleteUserInStorage(int id);

    void updateUserInStorage(User user);

    Collection<User> getAllUsersInStorage();

    User getUserInStorage(int id);
}
