package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.repositories.user.UserStorage;
import ru.yandex.practicum.filmorate.exception.CreationException;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") final UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User getUser(Long userId) throws NotFoundException {
        return userStorage.getUser(userId);
    }

    public void addFriend(Long userId, Long friendId) throws NotFoundException {
        userStorage.addFriend(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) throws NotFoundException, InternalServerException {
        userStorage.removeFriend(userId, friendId);
    }

    public Collection<User> getFriends(Long userId) throws NotFoundException {
        return userStorage.getFriends(userId);
    }

    public Collection<User> getCommonFriends(Long userId, Long friendId) throws NotFoundException {
        return userStorage.getCommonFriends(userId, friendId);
    }

    public User createUser(User newUser) throws CreationException, InternalServerException {
        return userStorage.createUser(newUser);
    }

    public User updateUser(User updUser) throws CreationException, InternalServerException {
        return userStorage.updateUser(updUser);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }
}