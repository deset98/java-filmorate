package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.CreationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(final InMemoryUserStorage inMemoryUserStorage) {
        this.userStorage = inMemoryUserStorage;
    }

    public User getUser(Long userId) throws NotFoundException {
        return userStorage.getUser(userId);
    }

    public Collection<User> addFriend(Long userId, Long friendId) throws NotFoundException {
        return userStorage.addFriend(userId, friendId);
    }

    public Collection<User> removeFriend(Long userId, Long friendId) throws NotFoundException {
        return userStorage.removeFriend(userId, friendId);
    }

    public Collection<User> getFriends(Long userId) throws NotFoundException {
        return userStorage.getFriends(userId);
    }

    public Collection<User> getCommonFriends(Long userId, Long friendId) throws NotFoundException {
        return userStorage.getCommonFriends(userId, friendId);
    }

    public User createUser(User newUser) throws CreationException {
        return userStorage.createUser(newUser);
    }

    public User updateUser(User updUser) throws CreationException {
        return userStorage.updateUser(updUser);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }
}