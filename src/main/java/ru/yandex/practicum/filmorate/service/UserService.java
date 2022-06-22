package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.Validate;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public int createUser(User user) {
        Validate.validateOrGetException(user);
        return userStorage.addUserInStorage(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsersInStorage();
    }

    public void updateUser(User user) {
        Validate.validateOrGetException(user);
        userStorage.updateUserInStorage(user);
    }

    public void addFriend(int userId, int friendId) {
        User user = userStorage.getUserInStorage(userId);
        User friend = userStorage.getUserInStorage(friendId);
        user.addInFriends(friendId);
        friend.addInFriends(userId);
        userStorage.updateUserInStorage(user);
        userStorage.updateUserInStorage(friend);
    }

    public void removeFriend(int userId, int friendId) {
        User user = userStorage.getUserInStorage(userId);
        User friend = userStorage.getUserInStorage(friendId);
        user.removeInFriends(friendId);
        friend.removeInFriends(userId);
        userStorage.updateUserInStorage(user);
        userStorage.updateUserInStorage(friend);
    }

    public List<User> getAllFriends(int userId) {
        User user = userStorage.getUserInStorage(userId);
        List<User> friends = new ArrayList<>();
        for (int id : user.getFriends()) {
            friends.add(userStorage.getUserInStorage(id));
        }
        return friends;
    }

    public List<User> getCommonFriends(int userId, int otherUserId) {
        User user = userStorage.getUserInStorage(userId);
        User otherUser = userStorage.getUserInStorage(otherUserId);
        List<User> commonFriends = new ArrayList<>();
        for (int idUser : user.getFriends()) {
            for (int idOtherUser : otherUser.getFriends()) {
                if (idUser == idOtherUser) {
                    commonFriends.add(userStorage.getUserInStorage(idUser));
                }
            }
        }
        return commonFriends;
    }

    public User getUser(int id) {
        return userStorage.getUserInStorage(id);
    }

}
