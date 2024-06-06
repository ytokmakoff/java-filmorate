package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString
public class User {
    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
