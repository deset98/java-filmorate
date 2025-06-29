package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.repositories.genre.GenreStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
@Slf4j
public class GenreService {
    private final GenreStorage genreStorage;

    @Autowired
    public GenreService(@Qualifier("GenreDbStorage") final GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Genre getGenreById(Long id) {
        Genre genre = genreStorage.getGenreById(id);
        if (genre == null) {
            throw new NotFoundException("Genre with id " + id + " not found");
        }
        return genre;
    }

    public List<Genre> getAllGenres() {
        return genreStorage.getAllGenres().stream()
                .sorted((g1, g2) -> (int) (g1.getId() - g2.getId()))
                .toList();
    }
}
