package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.JdbcFilmRepository;
import ru.yandex.practicum.filmorate.dao.JdbcUserRepository;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final JdbcFilmRepository jdbcFilmRepository;
    private final JdbcUserRepository jdbcUserRepository;

    public Film createFilm(Film film) throws ValidationException {
        validateFilm(film);
        log.info("Validated film: {}", film);
        jdbcFilmRepository.create(film);
        log.info("Saved film: {}", film);
        return film;
    }

    public Film updateFilm(Film film) throws ValidationException {
        if (jdbcFilmRepository.getById(film.getId()).isEmpty()) {
            log.warn("Film not found: {}", film);
            throw new FilmNotFoundException("Film not found");
        }
        validateFilm(film);
        log.info("Validated film: {}", film);
        jdbcFilmRepository.update(film);
        log.info("Updated film: {}", film);
        return film;
    }

    public Film filmById(int id) {
        if (jdbcFilmRepository.getById(id).isEmpty()) {
            log.warn("Film with id: {} not found", id);
            throw new FilmNotFoundException("Film not found");
        }
        Film film = jdbcFilmRepository.getById(id).get();
        log.info("Retrieved film by id: {} {}", id, film);
        return film;
    }

    public List<Film> getAllFilms() {
        List<Film> films = jdbcFilmRepository.getAll();
        log.info("Retrieved all films: total {}", films.size());
        return films;
    }

    public void likeFilm(int id, int userId) {
        if (jdbcFilmRepository.getById(id).isEmpty()) {
            log.warn("Film with id: {} not found", id);
            throw new FilmNotFoundException("film not found");
        }
        if (jdbcUserRepository.getById(userId).isEmpty()) {
            log.warn("User with id: {} not found", userId);
            throw new UserNotFoundException("user not found");
        }
        log.info("User with id: {} likes film with id: {}", id, userId);
        jdbcFilmRepository.addLike(id, userId);
        log.info("Added like to film with id: {} by user with id: {}", id, userId);
    }

    public void removeLike(int id, int userId) {
        log.info("User with id: {} removes film with id: {}", userId, id);
        jdbcFilmRepository.deleteLike(id, userId);
        log.info("Removed like from film with id: {} by user with id: {}", id, userId);
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> films = jdbcFilmRepository.getPopular(count);
        log.info("Retrieving {} popular films", films.size());
        return films;
    }

    private boolean validateFilm(Film film) throws ValidationException {
        if (film.getName().isBlank()) {
            log.warn("Validation failed: film name is blank");
            throw new ValidationException("film name is blank");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Validation failed: film description length: {} > 200", film.getDescription().length());
            throw new ValidationException("Film description length > 200");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Validation failed: film release date: {} is before 1895.12.28", film.getReleaseDate());
            throw new ValidationException("film release date is before 1895.12.28");
        }
        if (film.getGenres() != null) {
            for (GenreDto genre : film.getGenres()) {
                if (genre.getId() > 6 || genre.getId() < 0) {
                    log.warn("Validation failed: film genre id {} out of range", genre.getId());
                    throw new ValidationException("Film genre out of range");
                }
            }
        }
        if (film.getMpa() != null) {
            if (film.getMpa().getId() > 5 || film.getMpa().getId() < 0) {
                log.warn("Validation failed: film map id {} out of range", film.getMpa().getId());
                throw new ValidationException("film mpa id > 5");
            }
        }
        if (film.getDuration() < 0) {
            log.warn("Validation failed: film duration: {} < 0", film.getDuration());
            throw new ValidationException("film duration: < 0");
        }
        return true;
    }
}