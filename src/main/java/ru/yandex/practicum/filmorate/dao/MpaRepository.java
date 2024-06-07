package ru.yandex.practicum.filmorate.dao;


import ru.yandex.practicum.filmorate.dto.MpaRatingDto;

import java.util.List;
import java.util.Optional;

public interface MpaRepository {
    Optional<MpaRatingDto> getById(int id);

    List<MpaRatingDto> getAll();
}
