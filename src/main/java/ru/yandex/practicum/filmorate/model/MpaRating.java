package ru.yandex.practicum.filmorate.model;

import lombok.Getter;

@Getter
public enum MpaRating {
    G(1),
    PG(2),
    PG13(3),
    R(4),
    NC17(5);

    private final int id;

    MpaRating(int id) {
        this.id = id;
    }

    public static String ratingIdToStringName(int ratingId) {
        return switch (ratingId) {
            case 1 -> "G";
            case 2 -> "PG";
            case 3 -> "PG-13";
            case 4 -> "R";
            case 5 -> "NC-17";
            default -> throw new IllegalStateException("Unexpected value: " + ratingId);
        };
    }
}