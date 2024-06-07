package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum FilmGenre {
    Comedy(1),
    Drama(2),
    Cartoon(3),
    Thriller(4),
    Documentary(5),
    Action(6);

    private final int id;

    FilmGenre(int id) {
        this.id = id;
    }

    public static String filmGenreIdToString(int id) {
        return switch (id) {
            case 1 -> "Комедия";
            case 2 -> "Драма";
            case 3 -> "Мультфильм";
            case 4 -> "Триллер";
            case 5 -> "Документальный";
            case 6 -> "Боевик";
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };
    }
}