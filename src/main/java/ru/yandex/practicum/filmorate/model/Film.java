package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.ToString;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.LikeDto;
import ru.yandex.practicum.filmorate.dto.MpaRatingDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class Film {
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private MpaRatingDto mpa;
    private List<LikeDto> likes = new ArrayList<>();
    private List<GenreDto> genres = new ArrayList<>();
}
