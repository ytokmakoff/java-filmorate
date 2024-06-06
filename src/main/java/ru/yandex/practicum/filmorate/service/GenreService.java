package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.JdbcGenreRepository;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final JdbcGenreRepository jdbc;

    public GenreDto getById(int id) {
        Optional<GenreDto> genre = jdbc.getById(id);
        if (genre.isEmpty())
            throw new GenreNotFoundException("genre not found");
        return genre.get();
    }

    public List<GenreDto> findAll() {
        return jdbc.findAll();
    }
}
