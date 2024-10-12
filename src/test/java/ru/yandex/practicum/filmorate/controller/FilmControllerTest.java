package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeAll;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;


class FilmControllerTest {
    private FilmController filmController;
    private static Film validFilm;
    private static Film invalidFilm;
    Collection<Film> films;

    @BeforeAll
    static void setUpFilms() {
        validFilm = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(1900, 1, 1))
                .duration(10000)
                .build();

        invalidFilm = Film.builder()
                .name("name")
                .description("descriptionw")
                .releaseDate(LocalDate.of(1800, 1, 1))
                .duration(-76668)
                .build();
    }

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
    }

    @Test
    void mustCreateValidFilm() {
        assertDoesNotThrow(() -> filmController.addFilm(validFilm));

        films = filmController.getFilms();
        assertTrue(films.contains(validFilm));
    }

    @Test
    void mustnotCreateInvalidFilm() {
        Exception exception = assertThrows(ValidationException.class, () -> filmController.addFilm(invalidFilm));
        assertTrue(exception.getMessage().contains("Дата релиза указана ранее 28 декабря 1895 года")
                || exception.getMessage().contains("Длина описания фильма больше 200 символов")
                || exception.getMessage().contains("Продолжительность фильма должна быть положительным числом"));

        films = filmController.getFilms();
        assertFalse(films.contains(invalidFilm));
    }

    @Test
    void mustUpdateValidFilm() {
        assertDoesNotThrow(() -> filmController.updateFilm(validFilm));

        films = filmController.getFilms();
        assertTrue(films.contains(validFilm));
    }

    @Test
    void mustnotUpdateInvalidFilm() {
        Exception exception = assertThrows(ValidationException.class, () -> filmController.updateFilm(invalidFilm));
        assertTrue(exception.getMessage().contains("Дата релиза указана ранее 28 декабря 1895 года")
                || exception.getMessage().contains("Длина описания фильма больше 200 символов")
                || exception.getMessage().contains("Продолжительность фильма должна быть положительным числом"));

        films = filmController.getFilms();
        assertFalse(films.contains(invalidFilm));
    }
}