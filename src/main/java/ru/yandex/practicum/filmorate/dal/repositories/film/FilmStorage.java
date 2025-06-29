package ru.yandex.practicum.filmorate.dal.repositories.film;

import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Film getFilm(Long filmId);

    void likeFilm(Long filmId, Long userId);

    void unlikeFilm(Long filmId, Long userId);

    Collection<Film> getPopularFilms(Long count);

    Film addFilm(Film newFilm) throws InternalServerException;

    Film updateFilm(Film updFilm) throws InternalServerException;

    Collection<Film> getAllFilms();
}