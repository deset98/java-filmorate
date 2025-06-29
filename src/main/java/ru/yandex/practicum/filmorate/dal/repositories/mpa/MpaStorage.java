package ru.yandex.practicum.filmorate.dal.repositories.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {
    List<Mpa> getAllMpa();

    Mpa getMpaById(Long id);
}