package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.CreationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ParameterNotValidException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.Collection;

@Service
public class FilmService {
    public final FilmStorage filmStorage;

    @Autowired
    public FilmService(final InMemoryFilmStorage inMemoryFilmStorage) {
        this.filmStorage = inMemoryFilmStorage;
    }

    public Film getFilm(Long filmId) throws NotFoundException {
        return filmStorage.getFilm(filmId);
    }

    public Film likeFilm(Long filmId, Long userId) throws NotFoundException {
        return filmStorage.likeFilm(filmId, userId);
    }

    public Film unlikeFilm(Long filmId, Long userId) throws NotFoundException {
        return filmStorage.unlikeFilm(filmId, userId);
    }

    public Collection<Film> getPopularFilms(Long count) throws ParameterNotValidException {
        return filmStorage.getPopularFilms(count);
    }

    public Film addFilm(Film newFilm) throws CreationException {
        return filmStorage.addFilm(newFilm);
    }

    public Film updateFilm(Film updFilm) throws CreationException {
        return filmStorage.updateFilm(updFilm);
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }
}