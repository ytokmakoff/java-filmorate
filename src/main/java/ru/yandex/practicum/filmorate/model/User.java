package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@ToString
public class User {
    private final Set<Integer> friends = new HashSet<>();
    private int id;
    private String email;
    private String login;
    private String name;
    private String birthday;
}
