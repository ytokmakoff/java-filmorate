package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film saveFilm(Film film);

    Film updateExistingFilm(Film film) throws ValidationException;

    List<Film> getAllFilms();
}
