package ru.yandex.practicum.filmorate.dal.repositories.genre;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.repositories.BaseRepository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Repository
@Qualifier("GenreDbStorage")
public class GenreDbStorage extends BaseRepository<Genre> implements GenreStorage {

    public static final String GET_ALL_GENRES = "SELECT * FROM genres";
    public static final String GET_GENRE_BY_ID = "SELECT * FROM genres WHERE genre_id = ?";

    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Genre getGenreById(Long id) {
        return findOne(GET_GENRE_BY_ID, id);
    }

    @Override
    public List<Genre> findManyGenresByFilmId(String query, Long filmId) {
        return findMany(query, filmId);
    }

    @Override
    public List<Genre> getAllGenres() {
        return findMany(GET_ALL_GENRES);
    }
}
