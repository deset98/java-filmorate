package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.CreationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ParameterNotValidException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final InMemoryUserStorage userStorage;
    private final Map<Long, Film> films = new HashMap<>();

    public InMemoryFilmStorage(final UserStorage userStorage) {
        this.userStorage = (InMemoryUserStorage) userStorage;
    }

    @Override
    public Film getFilm(Long filmId) throws NotFoundException {
        findFilm(filmId);
        return films.get(filmId);
    }

    @Override
    public Film likeFilm(Long filmId, Long userId) throws NotFoundException {
        findFilm(filmId);
        userStorage.findUser(userId);
        films.get(filmId).getLikes().add(userId);
        return films.get(filmId);
    }

    @Override
    public Film unlikeFilm(Long filmId, Long userId) throws NotFoundException {
        findFilm(filmId);
        userStorage.findUser(userId);
        films.get(filmId).getLikes().remove(userId);
        return films.get(filmId);
    }

    @Override
    public Collection<Film> getPopularFilms(Long count) throws ParameterNotValidException {
        if (count <= 0) {
            throw new ParameterNotValidException(
                    "Параметр count не может быть меньше либо равен 0. count =" + count
            );
        }

        List<Film> topOfFilms = films.values().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .toList();

        if (topOfFilms.size() <= count) {
            return topOfFilms;
        } else {
            return topOfFilms.subList(1, count.intValue());
        }
    }

    @Override
    public Film addFilm(Film newFilm) throws CreationException {
        validateFilm(newFilm);
        newFilm.setId(getNextId());
        films.put(newFilm.getId(), newFilm);
        log.info("Добавлен новый фильм ID: {} - {}", newFilm.getId(), newFilm.getName());
        return newFilm;
    }

    @Override
    public Film updateFilm(Film updFilm) throws CreationException {
        findFilm(updFilm.getId());
        validateFilm(updFilm);
        films.put(updFilm.getId(), updFilm);
        log.info("Обновлены данные фильма ID: {} - {}", updFilm.getId(), updFilm.getName());
        return updFilm;
    }

    @Override
    public Collection<Film> getFilms() {
        return films.values();
    }

    private void findFilm(Long filmId) throws NotFoundException {
        String exceptionMessage;
        if (!films.containsKey(filmId)) {
            exceptionMessage = "Фильм не найден. ID: " + filmId;
            log.info("Исключение при получении фильма ID {}, '{}'", filmId, exceptionMessage);
            throw new NotFoundException(exceptionMessage);
        }
    }

    private void validateFilm(Film film) throws CreationException {
        String exceptionMessage;

        // при создании и обновлении
        if (film.getDescription().length() > 200) {
            exceptionMessage = "Длина описания фильма больше 200 символов";
            log.warn("Исключение при добавлении/изменении фильма ID {}, '{}'", film.getId(), exceptionMessage);
            throw new CreationException(exceptionMessage);
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            exceptionMessage = "Дата релиза указана ранее 28 декабря 1895 года";
            log.warn("Исключение при добавлении/изменении фильма ID {}, '{}'", film.getId(), exceptionMessage);
            throw new CreationException(exceptionMessage);
        }
        if (film.getDuration() <= 0) {
            exceptionMessage = "У фильма указана отрицательная длительность";
            log.warn("Исключение при добавлении/изменении фильма ID {}, '{}'", film.getId(), exceptionMessage);
            throw new CreationException("Продолжительность фильма должна быть положительным числом");
        }

        // только при обновлении
        if (film.getId() > 0 && !films.containsKey(film.getId())) {
            exceptionMessage = "Фильма с таким ID не существует";
            log.warn("Исключение при изменении фильма ID {}, '{}'", film.getId(), exceptionMessage);
            throw new CreationException(exceptionMessage);
        }
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}