package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.repositories.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
@Slf4j
public class MpaService {
    private final MpaStorage mpaStorage;

    @Autowired
    public MpaService(@Qualifier("MpaDbStorage") final MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public Mpa getMpaById(Long id) {
        Mpa mpa = mpaStorage.getMpaById(id);
        if (mpa == null) {
            throw new NotFoundException("MPA with id " + id + " not found");
        }
        return mpa;
    }

    public List<Mpa> getAllMpa() {
        return mpaStorage.getAllMpa();
    }
}
