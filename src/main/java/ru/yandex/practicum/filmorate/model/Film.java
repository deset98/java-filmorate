package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
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
}