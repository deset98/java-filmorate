package ru.yandex.practicum.filmorate.dal.repositories.mpa;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.repositories.BaseRepository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Repository
@Qualifier("MpaDbStorage")
public class MpaDbStorage extends BaseRepository<Mpa> implements MpaStorage {

    public static final String FIND_ALL = "SELECT * FROM mpa";
    public static final String FIND_BY_ID = "SELECT * FROM mpa WHERE mpa_id = ?";

    public MpaDbStorage(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Mpa getMpaById(Long id) {
        return findOne(FIND_BY_ID, id);
    }

    @Override
    public List<Mpa> getAllMpa() {
        return findMany(FIND_ALL);
    }
}
