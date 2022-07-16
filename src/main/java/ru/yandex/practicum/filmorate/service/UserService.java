package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Relationship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.relationship.RelationshipDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.Validate;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final RelationshipDbStorage relationshipDbStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage,
                       RelationshipDbStorage relationshipDbStorage) {
        this.userStorage = userStorage;
        this.relationshipDbStorage = relationshipDbStorage;
    }

    public User createUser(User user) {
        User newUser = ifEmptyNameRename(user);
        Validate.validateOrGetException(newUser);
        int idUser = userStorage.addUserInStorage(newUser);
        log.info("Создание пользователя {}      Пользователь с ID {} создан успешно", LocalDateTime.now(), idUser);
        return userStorage.getUserInStorage(idUser);
    }

    public Collection<User> getAllUsers() {
        Collection<User> users;
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
        if(userId<=0 || friendId<=0){
            throw new NoSuchElementException("Пользователь или друг с таким ID не найден");
        }
        User user = userStorage.getUserInStorage(userId);
        User friend = userStorage.getUserInStorage(friendId);
        Relationship newFriends = new Relationship(userId, friendId, true);
        //user.addInFriends(friendId);
        //friend.addInFriends(userId);
        //userStorage.updateUserInStorage(user);
        //userStorage.updateUserInStorage(friend);
        Set<Relationship> friends = new HashSet<Relationship>(relationshipDbStorage.getAllFriends(userId));
        if(friends.contains(newFriends)) {
           throw new ValidationException("Ошибка: у пользователя c ID " + userId + " уже есть друг с ID " + friendId);
        }
        relationshipDbStorage.addFriend(userId, friendId);
        log.info("Добавление в друзья {}      Пользователь с ID {} добавил друга с ID {} успешно",
                LocalDateTime.now(),
                userId, friendId);
    }

    public void removeFriend(int userId, int friendId) {
        User user = userStorage.getUserInStorage(userId);
        User friend = userStorage.getUserInStorage(friendId);
        Relationship newFriends = new Relationship(userId, friendId, true);
       // user.removeInFriends(friendId);
        //friend.removeInFriends(userId);
        //userStorage.updateUserInStorage(user);
        //userStorage.updateUserInStorage(friend);
        Set<Relationship> friends = new HashSet<Relationship>(relationshipDbStorage.getAllFriends(userId));
        if(!friends.contains(newFriends)) {
            throw new ValidationException("Ошибка: у пользователя c ID " + userId + " нет друга с ID " + friendId);
        }
        relationshipDbStorage.removeFriend(userId, friendId);
        log.info("Удаление из друзей {}      Пользователь с ID {} удалил друга с ID {} успешно",
                LocalDateTime.now(),
                userId, friendId);
    }

    public List<User> getAllFriends(int userId) {
        if(userId<=0){
            throw new NoSuchElementException("Пользователь с таким ID не найден");
        }
        User user = userStorage.getUserInStorage(userId);
        List<User> friends = new ArrayList<>();
        //for (int id : user.getFriends().keySet()) {
        //    friends.add(userStorage.getUserInStorage(id));
        //}
        for (Relationship r: relationshipDbStorage.getAllFriends(userId)){
            friends.add(userStorage.getUserInStorage(r.getFriendId()));
        }
        log.info("Получение списка друзей {}    пользователя с ID {} выполнено успешно",
                LocalDateTime.now(),
                userId);
        return friends;
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        if(userId<=0 || otherId<=0){
            throw new NoSuchElementException("Пользователь с таким ID не найден");
        }
        User user = userStorage.getUserInStorage(userId);
        User otherUser = userStorage.getUserInStorage(otherId);
        List<Integer> commonFriendsId = new ArrayList<>(relationshipDbStorage.getCommonFriends(userId, otherId));
        List<User> commonFriends = new ArrayList<>();
        for (int idUser : commonFriendsId) {
            commonFriends.add(userStorage.getUserInStorage(idUser));
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
        if(id<=0){
            throw new NoSuchElementException("Пользователь с таким ID " + id + " не найден");
        }
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
