package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController userController;
    private static User validUser;
    private static User invalidUser;
    private Collection<User> users;

    @BeforeAll
    static void setUpUsers() {
        validUser = User.builder()
                .email("email@email.ru")
                .login("login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        invalidUser = User.builder()
                .email("email@email.ru")
                .login("login")
                .birthday(LocalDate.of(2100, 1, 1))
                .build();
    }

    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    @Test
    void mustCreateValidUser() {
        assertDoesNotThrow(() -> userController.createUser(validUser));

        users = userController.getAllUsers();
        assertTrue(users.contains(validUser));
    }

    @Test
    void mustnotCreateInvalidUser() {
        Exception exception = assertThrows(ValidationException.class,
                () -> userController.createUser(invalidUser));
        assertTrue(exception.getMessage().contains("Дата рождения установлена в будущем"));

        users = userController.getAllUsers();
        assertFalse(users.contains(validUser));
    }

    @Test
    void mustUpdateValidUser() {
        assertDoesNotThrow(() -> userController.updateUser(validUser));

        users = userController.getAllUsers();
        assertTrue(users.contains(validUser));
    }

    @Test
    void mustnotUpdateInvalidUser() {
        Exception exception = assertThrows(ValidationException.class,
                () -> userController.createUser(invalidUser));
        assertTrue(exception.getMessage().contains("Дата рождения установлена в будущем"));

        users = userController.getAllUsers();
        assertFalse(users.contains(validUser));
    }
}