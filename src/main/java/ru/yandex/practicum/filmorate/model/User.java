package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class User {

    private Long id;

    @NotBlank(message = "не введен e-mail пользователя")
    @Email(message = "введен некорректный e-mail")
    private String email;

    @NotBlank(message = "не введен login пользователя")
    private String login;

    @NotBlank
    private String name;

    @NotNull(message = "не установлен день рождения пользователя")
    @Past
    private LocalDate birthday;

    private final Set<Long> friends = new HashSet<>();
}