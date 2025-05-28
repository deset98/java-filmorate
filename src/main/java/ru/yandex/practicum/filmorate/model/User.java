package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class User {

    private long id;
    @NotBlank(message = "не введен e-mail пользователя")
    @Email(message = "введен некорректный e-mail")
    private String email;
    @NotBlank(message = "не введен login пользователя")
    private String login;
    private String name;
    @NotNull(message = "не установлен день рождения пользователя")
    private LocalDate birthday;
}