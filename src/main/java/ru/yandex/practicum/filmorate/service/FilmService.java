package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.repositories.film.FilmStorage;
import ru.yandex.practicum.filmorate.exception.CreationException;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ParameterNotValidException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") final FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film getFilm(Long filmId) throws NotFoundException {
        return filmStorage.getFilm(filmId);
    }

    public void likeFilm(Long filmId, Long userId) throws NotFoundException {
        filmStorage.likeFilm(filmId, userId);
    }

    public void unlikeFilm(Long filmId, Long userId) throws NotFoundException {
        filmStorage.unlikeFilm(filmId, userId);
    }

    public Collection<Film> getPopularFilms(Long count) throws ParameterNotValidException {
        return filmStorage.getPopularFilms(count);
    }

    public Film addFilm(Film newFilm) throws CreationException, InternalServerException {
        return filmStorage.addFilm(newFilm);
    }

    public Film updateFilm(Film updFilm) throws CreationException, InternalServerException {
        return filmStorage.updateFilm(updFilm);
    }

    public Collection<Film> getFilms() {
        return filmStorage.getAllFilms();
    }
}