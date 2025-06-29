package ru.yandex.practicum.filmorate.dal.repositories.user;

import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    User getUser(Long userId);

    void addFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId) throws InternalServerException;

    Collection<User> getFriends(Long userId);

    Collection<User> getCommonFriends(Long userId, Long friendId);

    User createUser(User newUser) throws InternalServerException;

    User updateUser(User updUser) throws InternalServerException;

    Collection<User> getAllUsers();
}