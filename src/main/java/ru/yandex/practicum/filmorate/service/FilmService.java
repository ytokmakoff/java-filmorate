package ru.yandex.practicum.filmorate.service;

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
public class FilmService {
    private final JdbcFilmRepository jdbcFilmRepository;
    private final JdbcUserRepository jdbcUserRepository;
    public FilmService(JdbcFilmRepository jdbcFilmRepository, JdbcUserRepository jdbcUserRepository) {
        this.jdbcFilmRepository = jdbcFilmRepository;
        this.jdbcUserRepository = jdbcUserRepository;
    }

    public Film createFilm(Film film) throws ValidationException {
        validateFilm(film);
        jdbcFilmRepository.create(film);
        log.info("film {} saved", film);
        return film;
    }
    public Film updateFilm(Film film) throws ValidationException {
        if (jdbcFilmRepository.getById(film.getId()).isEmpty()) {
            log.warn("film: {} not found", film);
            throw new FilmNotFoundException("Film not found");
        }
        validateFilm(film);
        jdbcFilmRepository.update(film);
        log.info("film {} updated", film);
        return film;
    }

    public Film filmById(int id) {
        if (jdbcFilmRepository.getById(id).isEmpty()) {
            log.warn("film with id: {} not found", id);
            throw new FilmNotFoundException("Film not found");
        }
        log.info("film with id: {} received", id);
        return jdbcFilmRepository.getById(id).get();
    }

    public List<Film> getAllFilms() {
        log.info("all films received");
        return jdbcFilmRepository.getAll();
    }

    public void likeFilm(int id, int userId) {
        if (jdbcFilmRepository.getById(id).isEmpty())
            throw new FilmNotFoundException("film not found");
        if (jdbcUserRepository.getById(userId).isEmpty())
            throw new UserNotFoundException("user not found");
        log.info("Film and user exist, proceeding to add like...");
        jdbcFilmRepository.addLike(id, userId);
    }

    public void removeLike(int id, int userId) {
        jdbcFilmRepository.deleteLike(id, userId);
    }

    public List<Film> getPopularFilms(int count) {
        log.info("Retrieving list of popular films");
        return jdbcFilmRepository.getPopular(count);
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
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("film release date: {} is before 1895.12.28", film.getReleaseDate());
            throw new ValidationException("film release date is before 1895.12.28");
        }
        if (film.getGenres() != null) {
            for (GenreDto genre : film.getGenres()) {
                if (genre.getId() > 6) {
                    log.warn("film rating > 6 {}", genre.getId());
                    throw new ValidationException("film rating > 6");
                }
                if (genre.getId() < 0) {
                    log.warn("film rating > 0 {}", genre.getId());
                    throw new ValidationException("film rating > 0");
                }
            }
        }
        if (film.getMpa() != null) {
            if (film.getMpa().getId() > 5) {
                log.warn("film mpaId > 5: mapId = {}", film.getMpa().getId());
                throw new ValidationException("film mpa id > 5");
            }
            if (film.getMpa().getId() < 0) {
                log.warn("film mpaId < 0: mpaId = {}", film.getMpa().getId());
                throw new ValidationException("film mpa id < 0");
            }
        }
        if (film.getDuration() < 0) {
            log.warn("film duration: {} < 0", film.getDuration());
            throw new ValidationException("film duration: < 0");
        }
        return true;
    }
}