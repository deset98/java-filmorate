package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


class FilmControllerTest {
    FilmController filmController;
    Film film;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
    }

    @Test
    void mustCreateValidMovie() {
        film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1900, 1, 1))
                .duration(10000)
                .build();
        assertDoesNotThrow(() -> filmController.validateFilm(film));
    }

    @Test
    void mustnotCreateInvalidMovie() {
        film = Film.builder()
                .name("name")
                .description("descriptionw")
                .releaseDate(LocalDate.of(1800, 1, 1))
                .duration(-76668)
                .build();
        Exception exception = assertThrows(ValidationException.class, () -> filmController.validateFilm(film));
        assertTrue(exception.getMessage().contains("Дата релиза указана ранее 28 декабря 1895 года")
                || exception.getMessage().contains("Длина описания фильма больше 200 символов")
                || exception.getMessage().contains("Продолжительность фильма должна быть положительным числом"));
    }
}