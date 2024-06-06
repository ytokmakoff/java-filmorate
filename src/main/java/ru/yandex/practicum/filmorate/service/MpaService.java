package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.JdbcMpaRepository;
import ru.yandex.practicum.filmorate.dto.MpaRatingDto;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final JdbcMpaRepository jdbc;

    public MpaRatingDto getById(int id) {
        Optional<MpaRatingDto> mpa = jdbc.getById(id);
        if (mpa.isEmpty())
            throw new MpaNotFoundException("mpa not found");
        return mpa.get();
    }

    public List<MpaRatingDto> getAll() {
        return jdbc.getAll();
    }
}
