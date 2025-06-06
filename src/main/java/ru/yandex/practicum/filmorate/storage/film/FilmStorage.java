package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Film getFilm(Long filmId);

    Film likeFilm(Long filmId, Long userId);

    Film unlikeFilm(Long filmId, Long userId);

    Collection<Film> getPopularFilms(Long count);

    Film addFilm(Film newFilm);

    Film updateFilm(Film updFilm);

    Collection<Film> getFilms();
}