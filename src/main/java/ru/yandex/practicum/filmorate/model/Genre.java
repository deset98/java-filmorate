package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Genre {

    private final Long id;

    @NotNull
    @NotBlank
    @NotEmpty
    private final String name;

}