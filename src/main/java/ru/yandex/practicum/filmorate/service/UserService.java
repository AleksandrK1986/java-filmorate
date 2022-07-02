package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.Validate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        User newUser = ifEmptyNameRename(user);
        Validate.validateOrGetException(newUser);
        int idUser = userStorage.addUserInStorage(newUser);
        log.info("Создание пользователя {}      Пользователь с ID {} создан успешно", LocalDateTime.now(), idUser);
        return userStorage.getUserInStorage(idUser);
    }

    public List<User> getAllUsers() {
        List<User> users;
        users = userStorage.getAllUsersInStorage();
        if (users != null) {
            log.info("Получение списка пользователей {}      Пользователи получены успешно", LocalDateTime.now());
        } else {
            log.info("Получение списка пользователей {}      Пользователи отсутствуют", LocalDateTime.now());
        }
        return users;
    }

    public User updateUser(User user) {
        User newUser = ifEmptyNameRename(user);
        Validate.validateOrGetException(newUser);
        userStorage.updateUserInStorage(newUser);
        log.info("Обновление пользователя {}      Пользователь с ID {} обновлен успешно",
                LocalDateTime.now(), newUser.getId());
        return userStorage.getUserInStorage(newUser.getId());
    }

    public void addFriend(int userId, int friendId) {
        User user = userStorage.getUserInStorage(userId);
        User friend = userStorage.getUserInStorage(friendId);
        user.addInFriends(friendId);
        friend.addInFriends(userId);
        userStorage.updateUserInStorage(user);
        userStorage.updateUserInStorage(friend);
        log.info("Добавление в друзья {}      Пользователь с ID {} добавил друга с ID {} успешно",
                LocalDateTime.now(),
                userId, friendId);
    }

    public void removeFriend(int userId, int friendId) {
        User user = userStorage.getUserInStorage(userId);
        User friend = userStorage.getUserInStorage(friendId);
        user.removeInFriends(friendId);
        friend.removeInFriends(userId);
        userStorage.updateUserInStorage(user);
        userStorage.updateUserInStorage(friend);
        log.info("Удаление из друзей {}      Пользователь с ID {} удалил друга с ID {} успешно",
                LocalDateTime.now(),
                userId, friendId);
    }

    public List<User> getAllFriends(int userId) {
        User user = userStorage.getUserInStorage(userId);
        List<User> friends = new ArrayList<>();
        for (int id : user.getFriends().keySet()) {
            friends.add(userStorage.getUserInStorage(id));
        }
        log.info("Получение списка друзей {}    пользователя с ID {} выполнено успешно",
                LocalDateTime.now(),
                userId);
        return friends;
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        User user = userStorage.getUserInStorage(userId);
        User otherUser = userStorage.getUserInStorage(otherId);
        List<User> commonFriends = new ArrayList<>();
        for (int idUser : user.getFriends().keySet()) {
            for (int idOtherUser : otherUser.getFriends().keySet()) {
                if (idUser == idOtherUser) {
                    commonFriends.add(userStorage.getUserInStorage(idUser));
                }
            }
        }
        if (commonFriends == null) {
            log.info("Получение списка общих друзей {}    пользователи с ID {} и с ID {} не имеют общих друзей",
                    LocalDateTime.now(),
                    userId, otherId);
        } else {
            log.info("Получение списка общих друзей {}    пользователя с ID {} и с ID {} выполнено успешно",
                    LocalDateTime.now(),
                    userId, otherId);
        }
        return commonFriends;
    }

    public User getUser(int id) {
        User user = userStorage.getUserInStorage(id);
        log.info("Получение пользователя {}      Пользователь с ID {} получен успешно", LocalDateTime.now(), id);
        return user;
    }

    private User ifEmptyNameRename(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return user;
    }

}
