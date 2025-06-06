package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.CreationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User getUser(Long userId) throws NotFoundException {
        findUser(userId);
        return users.get(userId);
    }

    @Override
    public Collection<User> addFriend(Long userId, Long friendId) throws NotFoundException {
        findUser(userId);
        findUser(friendId);
        users.get(userId).getFriends().add(friendId);
        users.get(friendId).getFriends().add(userId);
        return List.of(users.get(userId), users.get(friendId));
    }

    @Override
    public Collection<User> removeFriend(Long userId, Long friendId) throws NotFoundException {
        findUser(userId);
        findUser(friendId);
        users.get(friendId).getFriends().remove(userId);
        users.get(userId).getFriends().remove(friendId);
        return List.of(users.get(userId), users.get(friendId));
    }

    @Override
    public Collection<User> getFriends(Long userId) throws NotFoundException {
        findUser(userId);
        return users.get(userId).getFriends().stream()
                .map(users::get)
                .toList();
    }

    @Override
    public Collection<User> getCommonFriends(Long userId, Long friendId) throws NotFoundException {
        findUser(userId);
        findUser(friendId);

        return users.get(userId).getFriends().stream()
                .filter(id1 -> users.get(friendId).getFriends().contains(id1))
                .map(users::get)
                .toList();
    }

    @Override
    public User createUser(User newUser) throws CreationException {
        validateUser(newUser);
        newUser.setId(getNextId());
        users.put(newUser.getId(), newUser);
        log.info("Создан новый пользователь ID: {} - {}", newUser.getId(), newUser.getLogin());
        return newUser;
    }

    @Override
    public User updateUser(User updUser) throws CreationException {
        validateUser(updUser);

        users.put(updUser.getId(), updUser);
        log.info("Обновлены данные пользователя ID: {} - {}", updUser.getId(), updUser.getLogin());

        users.put(updUser.getId(), updUser);
        log.info("Обновлены данные пользователя ID: {} - {}", updUser.getId(), updUser.getLogin());
        return updUser;
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    private void validateUser(User user) throws CreationException, NotFoundException {
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
            throw new CreationException(exceptionMessage);
        }

        // только при обновлении
        if (user.getId() > 0 && !users.containsKey(user.getId())) {
            exceptionMessage = "Пользователя с таким ID не существует.";
            log.warn("Исключение при изменении пользователя ID {}, '{}'", user.getId(), exceptionMessage);
            throw new NotFoundException(exceptionMessage);
        }
    }

    public void findUser(Long userId) throws NotFoundException {
        String exceptionMessage;
        if (!users.containsKey(userId)) {
            exceptionMessage = "Пользователь не найден. ID: " + userId;
            log.info("Исключение при получении информации о пользователе ID {}, '{}'", userId, exceptionMessage);
            throw new NotFoundException(exceptionMessage);
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