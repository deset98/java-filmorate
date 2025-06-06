package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class Film {
    private long id;
    @NotBlank(message = "не введено название фильма")
    private String name;
    @NotBlank(message = "не введено описание фильма")
    private String description;
    @NotNull(message = "не установлена дата выхода фильма")
    private LocalDate releaseDate;
    @NotNull(message = "не установлена длительность фильма")
    private long duration;
    private Set<Long> likes = new HashSet<>();
}