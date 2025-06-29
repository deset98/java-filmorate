package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.CreationException;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable Long userId) {
        return userService.getUser(userId);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(
            @PathVariable Long userId,
            @PathVariable Long friendId)
            throws NotFoundException {
        userService.addFriend(userId, friendId);
    }


    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFriend(
            @PathVariable Long userId,
            @PathVariable Long friendId)
            throws NotFoundException, InternalServerException {
        userService.removeFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public Collection<User> getFriends(
            @PathVariable Long userId)
            throws NotFoundException {
        return userService.getFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{friendId}")
    public Collection<User> getCommonFriends(
            @PathVariable Long userId,
            @PathVariable Long friendId)
            throws NotFoundException {
        return userService.getCommonFriends(userId, friendId);
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User newUser) throws CreationException, InternalServerException {
        return userService.createUser(newUser);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User updUser) throws CreationException, InternalServerException {
        return userService.updateUser(updUser);
    }
}