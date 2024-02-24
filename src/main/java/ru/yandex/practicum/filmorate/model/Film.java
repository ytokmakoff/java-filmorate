package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
@Builder
@ToString
public class Film {
    private final Set<Integer> likes = new HashSet<>();
    private int id;
    private String name;
    private String description;
    private String releaseDate;
    private int duration;
}
