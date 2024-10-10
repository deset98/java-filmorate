package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film newFilm) throws ValidationException {
        validateFilm(newFilm);
        newFilm.setId(getNextId());
        films.put(newFilm.getId(), newFilm);
        log.info("Добавлен новый фильм ID: {} - {}", newFilm.getId(), newFilm.getName());
        return newFilm;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film updFilm) throws ValidationException {
        validateFilm(updFilm);
        films.put(updFilm.getId(), updFilm);
        log.info("Обновлены данные фильма ID: {} - {}", updFilm.getId(), updFilm.getName());
        return updFilm;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    private void validateFilm(Film film) throws ValidationException {
        String exceptionMessage;

        // при создании и обновлении
        if (film.getDescription().length() > 200) {
            exceptionMessage = "Длина описания фильма больше 200 символов";
            log.warn("Исключение при добавлении/изменении фильма ID {}, '{}'", film.getId(), exceptionMessage);
            throw new ValidationException(exceptionMessage);
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            exceptionMessage = "Дата релиза указана ранее 28 декабря 1895 года";
            log.warn("Исключение при добавлении/изменении фильма ID {}, '{}'", film.getId(), exceptionMessage);
            throw new ValidationException(exceptionMessage);
        }
        if (film.getDuration().toNanos() <= 0) {
            exceptionMessage = "У фильма указана отрицательная длительность";
            log.warn("Исключение при добавлении/изменении фильма ID {}, '{}'", film.getId(), exceptionMessage);
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }

        // только при обновлении
        if (film.getId() > 0 && !films.containsKey(film.getId())) {
            exceptionMessage = "Фильма с таким ID не существует";
            log.warn("Исключение при изменении фильма ID {}, '{}'", film.getId(), exceptionMessage);
            throw new ValidationException(exceptionMessage);
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