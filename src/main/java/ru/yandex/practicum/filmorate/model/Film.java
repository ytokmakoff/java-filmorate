package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.util.Set;

/**
 * Film.
 */
@Data
@Builder
@ToString
public class Film {
    private int id;
    private String name;
    private String description;
    private String releaseDate;
    private int duration;
    private Set<Integer> likes;
}
