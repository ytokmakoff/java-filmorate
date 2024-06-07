package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.JdbcMpaRepository;
import ru.yandex.practicum.filmorate.dto.MpaRatingDto;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaService {
    private final JdbcMpaRepository jdbc;

    public MpaRatingDto getById(int id) {
        Optional<MpaRatingDto> mpa = jdbc.getById(id);
        if (mpa.isEmpty()) {
            log.warn("Mpa with id: {} not found", id);
            throw new MpaNotFoundException("mpa not found");
        }
        log.warn("Retrieved mpa by id {} {}", id, mpa.get());
        return mpa.get();
    }

    public List<MpaRatingDto> getAll() {
        List<MpaRatingDto> mpaRatingDtoList = jdbc.getAll();
        log.info("Retrieved all mpa total: {}", mpaRatingDtoList.size());
        return mpaRatingDtoList;
    }
}
