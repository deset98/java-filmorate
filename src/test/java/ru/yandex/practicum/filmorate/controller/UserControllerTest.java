package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController userController;
    User user;

    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    @Test
    void mustCreateValidUser() {
        user = User.builder()
                .email("email@email.ru")
                .login("login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        assertDoesNotThrow(() -> userController.validateUser(user));
    }

    @Test
    void mustnotCreateInvalidUser() {
        user = User.builder()
                .email("email@email.ru")
                .login("login")
                .birthday(LocalDate.of(2100, 1, 1))
                .build();
        Exception exception = assertThrows(ValidationException.class, () -> userController.validateUser(user));
        assertTrue(exception.getMessage().contains("Дата рождения установлена в будущем"));
    }
}