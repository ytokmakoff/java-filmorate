package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.JdbcGenreRepository;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    private final JdbcGenreRepository jdbc;

    public GenreDto getById(int id) {
        Optional<GenreDto> genre = jdbc.getById(id);
        if (genre.isEmpty()) {
            log.warn("Film with id: {} not found", id);
            throw new GenreNotFoundException("genre not found");
        }
        log.info("Retrieved genre by id {} {}", id, genre.get());
        return genre.get();
    }

    public List<GenreDto> findAll() {
        List<GenreDto> genreDtoList = jdbc.findAll();
        log.info("Retrieved add genres total: {}", genreDtoList.size());
        return genreDtoList;
    }
}
