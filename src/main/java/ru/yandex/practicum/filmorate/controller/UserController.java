package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @PostMapping
    public User createUser(@Valid @RequestBody User newUser) throws ValidationException {
        validateUser(newUser);
        newUser.setId(getNextId());
        users.put(newUser.getId(), newUser);
        log.info("Создан новый пользователь ID: {} - {}", newUser.getId(), newUser.getLogin());
        return newUser;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User updUser) throws ValidationException {
        validateUser(updUser);

        users.put(updUser.getId(), updUser);
        log.info("Обновлены данные пользователя ID: {} - {}", updUser.getId(), updUser.getLogin());

        users.put(updUser.getId(), updUser);
        log.info("Обновлены данные пользователя ID: {} - {}", updUser.getId(), updUser.getLogin());
        return updUser;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }

    private void validateUser(User user) throws ValidationException {
        String exceptionMessage;

        // при создании и обновлении
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            log.info("Отсутствует имя пользователя: {}; Вместо имени установлен логин: {}",
                    user.getId(), user.getLogin());
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            exceptionMessage = "Дата рождения установлена в будущем";
            log.warn("Исключение при добавлении/изменении пользователя ID {}, '{}'", user.getId(), exceptionMessage);
            throw new ValidationException(exceptionMessage);
        }

        // только при обновлении
        if (user.getId() > 0 && !users.containsKey(user.getId())) {
            exceptionMessage = "Пользователя с таким ID не существует";
            log.warn("Исключение при изменении пользователя ID {}, '{}'", user.getId(), exceptionMessage);
            throw new ValidationException(exceptionMessage);
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}