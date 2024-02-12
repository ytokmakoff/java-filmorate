package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private int generateId = 1;
    Map<Integer, Film> filmMap = new HashMap<>();

    @PostMapping
    Film saveFilm(@RequestBody Film film) throws ValidationException {
        validateFilm(film);
        film.setId(generateId());
        log.info("Film created: {}", film);
        filmMap.put(film.getId(), film);

        return film;
    }

    @PutMapping
    Film updateExistingFilm(@RequestBody Film film) throws ValidationException {
        validateFilm(film);
        if (filmMap.containsKey(film.getId())) {
            filmMap.put(film.getId(), film);
            log.info("Film updated: {}", film);
        } else {
            log.warn("Film with id {} not exist", film.getId());
            throw new ValidationException("Film id not exist");
        }

        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Retrieving list of films");
        return new ArrayList<>(filmMap.values());
    }

    private boolean validateFilm(Film film) throws ValidationException {
        if (film.getName().isBlank()) {
            log.warn("film name is blank");
            throw new ValidationException("film name is blank");
        }
        if (film.getDescription().length() > 200) {
            log.warn("film description length: {} > 200", film.getDescription().length());
            throw new ValidationException("film description length > 200");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (LocalDate.parse(film.getReleaseDate(), formatter)
                .isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("film release date: {} is before 1895.12.28", film.getReleaseDate());
            throw new ValidationException("film release date is before 1895.12.28");
        }
        if (film.getDuration() < 0) {
            log.warn("film duration: {} < 0", film.getDuration());
            throw new ValidationException("film duration: < 0");
        }
        return true;
    }

    private int generateId() {
        return generateId++;
    }
}