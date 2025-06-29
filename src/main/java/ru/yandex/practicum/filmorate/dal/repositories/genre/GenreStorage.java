package ru.yandex.practicum.filmorate.dal.repositories.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    List<Genre> getAllGenres();

    Genre getGenreById(Long id);

    List<Genre> findManyGenresByFilmId(String findGenres, Long filmId);
}
