package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class User {
    int id;
    String email;
    String login;

    String name;
    String birthday;
}
