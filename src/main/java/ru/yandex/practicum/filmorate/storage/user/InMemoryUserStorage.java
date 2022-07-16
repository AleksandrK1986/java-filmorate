package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

@Component
@Qualifier("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private HashMap<Integer, User> usersInStorage = new HashMap<>();
    private static int counter = 0;

    @Override
    public int addUserInStorage(User user) {
        user.setId(getNewId());
        usersInStorage.put(user.getId(), user);
        return user.getId();
    }

    @Override
    public void deleteUserInStorage(int id) {
        if (usersInStorage.containsKey(id)) {
            usersInStorage.remove(id);
        } else {
            throw new NoSuchElementException("Пользователь с ID " + id + " не найден в хранилище");
        }
    }

    @Override
    public void updateUserInStorage(User user) {
        if (usersInStorage.containsKey(user.getId())) {
            usersInStorage.remove(user.getId());
            usersInStorage.put(user.getId(), user);
        } else {
            throw new NoSuchElementException("Пользователь с ID " + user.getId() + " не найден в хранилище");
        }
    }

    @Override
    public List<User> getAllUsersInStorage() {
        List<User> users = new ArrayList<>();
        for (User u : usersInStorage.values()) {
            users.add(u);
        }
        return users;
    }

    @Override
    public User getUserInStorage(int id) {
        if (usersInStorage.containsKey(id)) {
            return usersInStorage.get(id);
        } else {
            throw new NoSuchElementException("Пользователь с ID " + id + " не найден в хранилище");
        }
    }

    private int getNewId() {
        counter = counter + 1;
        return counter;
    }
}
