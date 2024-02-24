package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private int generateId = 1;
    private final InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage) {
        this.inMemoryFilmStorage = filmStorage;
    }

    public Film saveFilm(Film film) throws ValidationException {
        validateFilm(film);
        film.setId(generateId());

        inMemoryFilmStorage.saveFilm(film);

        return film;
    }

    public Film updateExistingFilm(Film film) throws ValidationException {
        validateFilm(film);
        inMemoryFilmStorage.updateExistingFilm(film);
        return film;
    }

    public Film filmById(int id) {
        if (inMemoryFilmStorage.getFilmById(id) == null) {
            throw new FilmNotFoundException("Film not found");
        }
        return inMemoryFilmStorage.getFilmById(id);
    }

    public List<Film> getAllFilms() {
        return inMemoryFilmStorage.getAllFilms();
    }

    public void likeFilm(int id, int userId) {
        if (inMemoryFilmStorage.getFilmById(id) != null) {
            inMemoryFilmStorage.getFilmById(id).getLikes().add(userId);
        } else {
            throw new FilmNotFoundException("Film not found");
        }
    }

    public void removeLike(int id, int userId) {
        if (inMemoryFilmStorage.getFilmById(id) != null) {
            inMemoryFilmStorage.getFilmById(id).getLikes().remove(userId);
        } else {
            throw new FilmNotFoundException("Film not found");
        }
    }

    public List<Film> getPopularFilms(int count) {
        return inMemoryFilmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingInt(o -> o.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
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
