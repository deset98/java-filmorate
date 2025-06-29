package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.validator.DateAfter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class Film {

    private Long id;

    @NotBlank(message = "не введено название фильма")
    private String name;

    @NotBlank(message = "не введено описание фильма")
    @Size(max = 200)
    private String description;

    @NotNull(message = "не установлена дата выхода фильма")
    @PastOrPresent
    @DateAfter(message = "Дата не может быть раньше 1895-12-28")
    private LocalDate releaseDate;

    @NotNull(message = "не установлена длительность фильма")
    @Positive
    private Long duration;

    private Mpa mpa;

    private List<Genre> genres = new ArrayList<>();

    private Set<Long> likes = new HashSet<>();
}