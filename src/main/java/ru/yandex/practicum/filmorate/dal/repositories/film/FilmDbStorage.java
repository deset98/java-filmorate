package ru.yandex.practicum.filmorate.dal.repositories.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.dal.repositories.BaseRepository;
import ru.yandex.practicum.filmorate.dal.repositories.genre.GenreStorage;
import ru.yandex.practicum.filmorate.dal.repositories.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
@Qualifier("FilmDbStorage")
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage {
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    private static final String INSERT_FILM =
            "INSERT INTO films (film_name, mpa_id, description, release_date, duration)" +
                    "VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_FILM =
            "UPDATE films " +
                    "SET film_name = ?, mpa_id = ?, description = ?, release_date = ?, duration = ?" +
                    "WHERE film_id = ?";

    public static final String GET_ALL_FILMS =
            "SELECT * FROM films AS f " +
                    "LEFT JOIN mpa AS m ON f.mpa_id=m.mpa_id ";

    public static final String GET_FILM =
            "SELECT * " +
                    "FROM films AS f " +
                    "LEFT JOIN mpa AS m ON f.mpa_id=m.mpa_id " +
                    "WHERE f.film_id = ?";

    private static final String INSERT_GENRES_OF_FILM =
            "INSERT INTO genres_of_film (film_id, genre_id) " +
                    "VALUES (?, ?)";

    public static final String GET_LIKES =
            "SELECT user_id " +
                    "FROM likes " +
                    "WHERE film_id=?";

    public static final String GET_GENRES =
            "SELECT * FROM genres " +
                    "WHERE genre_id " +
                    "IN (SELECT genre_id " +
                    "FROM genres_of_film " +
                    "WHERE film_id=?)";

    public static final String REMOVE_LIKE =
            "DELETE FROM likes " +
                    "WHERE user_id=? AND film_id=?";

    public static final String ADD_LIKE =
            "INSERT INTO likes (user_id, film_id) VALUES (?, ?)";

    public static final String TOP_FILMS =
            "SELECT film_id " +
                    "FROM (SELECT DISTINCT COUNT(film_id) AS cfi, film_id " +
                    "FROM likes " +
                    "GROUP BY film_id " +
                    "ORDER BY cfi DESC " +
                    "LIMIT ?)";


    public FilmDbStorage(JdbcTemplate jdbc,
                         FilmMapper mapper,
                         @Qualifier("MpaDbStorage") MpaStorage mpaStorage,
                         @Qualifier("GenreDbStorage") GenreStorage genreStorage) {
        super(jdbc, mapper);
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    @Override
    public Film addFilm(Film newFilm) throws InternalServerException {

        setMpaForFilm(newFilm);

        long id = insert(
                INSERT_FILM,
                newFilm.getName(),
                newFilm.getMpa().getId(),
                newFilm.getDescription(),
                newFilm.getReleaseDate().toString(),
                newFilm.getDuration()
        );
        newFilm.setId(id);

        setGenresForFilm(newFilm);

        return newFilm;
    }

    @Override
    public Film updateFilm(Film updFilm) throws InternalServerException {
        update(
                UPDATE_FILM,
                updFilm.getName(),
                updFilm.getMpa().getId(),
                updFilm.getDescription(),
                updFilm.getReleaseDate().toString(),
                updFilm.getDuration(),
                updFilm.getId()
        );


        return updFilm;
    }

    @Override
    public Film getFilm(Long filmId) {
        Film film;

        try {
            film = findOne(GET_FILM, filmId);
            if (film == null) {
                throw new NotFoundException("Film with id " + filmId + " not found");
            }
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }

        List<Genre> list = genreStorage.findManyGenresByFilmId(GET_GENRES, filmId);
        List<Long> likes = jdbc.queryForList(GET_LIKES, Long.class, filmId);

        film.getLikes().addAll(likes);
        film.getGenres().addAll(list);
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return findMany(GET_ALL_FILMS);
    }


    @Override
    public void likeFilm(Long filmId, Long userId) {
        jdbc.update(ADD_LIKE, userId, filmId);
    }

    @Override
    public void unlikeFilm(Long filmId, Long userId) {

        jdbc.update(REMOVE_LIKE, userId, filmId);
    }

    @Override
    public Collection<Film> getPopularFilms(Long count) {

        List<Film> topFilms = new ArrayList<>();
        for (Long id : jdbc.queryForList(TOP_FILMS, Long.class, count)) {
            topFilms.add(getFilm(id));
        }

        return topFilms;
    }


    private void setGenresForFilm(Film film) {
        List<Genre> fullListOfGenres = new ArrayList<>();
        if (!film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                Long genreId = genre.getId();
                if (genreStorage.getAllGenres().stream().anyMatch(g -> g.getId().equals(genreId))) {
                    fullListOfGenres.add(genreStorage.getGenreById(genreId));
                    jdbc.update(INSERT_GENRES_OF_FILM, film.getId(), genreId);
                } else {
                    throw new NotFoundException("Genre with id " + genreId + " not found");
                }
            }
            film.setGenres(fullListOfGenres);
        }
    }

    private void setMpaForFilm(Film film) {
        Long mpaId = film.getMpa().getId();
        if (mpaStorage.getAllMpa().stream().noneMatch(m -> m.getId().equals(mpaId))) {
            throw new NotFoundException("Mpa with id " + mpaId + " not found");
        }
        film.setMpa(mpaStorage.getMpaById(mpaId));
    }

}