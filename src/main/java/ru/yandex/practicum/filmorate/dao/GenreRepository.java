package ru.yandex.practicum.filmorate.dao;


import ru.yandex.practicum.filmorate.dto.GenreDto;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    Optional<GenreDto> getById(int id);

    List<GenreDto> findAll();
}
