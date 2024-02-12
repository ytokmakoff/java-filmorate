package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

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
