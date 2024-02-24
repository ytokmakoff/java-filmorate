package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film saveFilm(Film film) {
        films.put(film.getId(), film);
        log.info("Film saved: {}", film);
        return film;
    }

    @Override
    public Film updateExistingFilm(Film film) throws ValidationException {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Film updated: {}", film);
        } else {
            log.warn("Film with id {} not exist", film.getId());
            throw new ValidationException("Film id not exist");
        }
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        log.info("Retrieving list of films");
        return new ArrayList<>(films.values());
    }

    public Film getFilmById(int id) {
        return films.get(id);
    }
}
