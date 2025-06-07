package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    User getUser(Long userId);

    Collection<User> addFriend(Long userId, Long friendId);

    Collection<User> removeFriend(Long userId, Long friendId);

    Collection<User> getFriends(Long userId);

    Collection<User> getCommonFriends(Long userId, Long friendId);

    User createUser(User newUser);

    User updateUser(User updUser);

    Collection<User> getAllUsers();
}