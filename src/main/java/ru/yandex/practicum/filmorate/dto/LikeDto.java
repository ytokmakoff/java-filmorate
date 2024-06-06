package ru.yandex.practicum.filmorate.dto;

import lombok.Data;

@Data
public class LikeDto {
    private int userId;
    private int filmId;
}
