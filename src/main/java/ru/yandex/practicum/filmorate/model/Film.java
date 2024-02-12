package ru.yandex.practicum.filmorate.model;

import lombok.*;

/**
 * Film.
 */
@Data
@Builder
@ToString
public class Film {
    int id;
    String name;
    String description;
    String releaseDate;
    int duration;
}
