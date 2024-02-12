package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {
    FilmController filmController;

    @BeforeEach
    void init() {
        filmController = new FilmController();
    }
    @Test
    void correctValues() throws ValidationException {
        Film film = Film.builder()
                .name("nisi eiusmod")
                .description("adipisicing")
                .releaseDate("1967-03-25")
                .duration(100)
                .build();
        filmController.saveFilm(film);
        assertEquals(1, filmController.getAllFilms().size(), "incorrect list length");
        assertEquals(filmController.getAllFilms().get(0), film);
    }

    @Test
    void updateFilmCorrectValues() throws ValidationException {
        Film film = Film.builder()
                .name("nisi eiusmod")
                .description("adipisicing")
                .releaseDate("1967-03-25")
                .duration(100)
                .build();
        filmController.saveFilm(film);
        Film film2 = Film.builder()
                .name("new film")
                .description("qweqwe")
                .releaseDate("2000-03-25")
                .duration(120)
                .build();
        film2.setId(film.getId());
        filmController.updateExistingFilm(film2);
        assertEquals(1, filmController.getAllFilms().size(), "incorrect list length");
        assertEquals(filmController.getAllFilms().get(0), film2);
    }

    @Test
    void updateFilmIncorrectId() throws ValidationException {
        Film film = Film.builder()
                .name("nisi eiusmod")
                .description("adipisicing")
                .releaseDate("1967-03-25")
                .duration(100)
                .build();
        filmController.saveFilm(film);
        Film film2 = Film.builder()
                .name("new film")
                .description("qweqwe")
                .releaseDate("2000-03-25")
                .duration(120)
                .build();
        film2.setId(100);
        assertThrows(ValidationException.class, () -> filmController.updateExistingFilm(film2));
    }
    @Test
    void nameCanNotBeEmpty() {
        Film film = Film.builder()
                .name("")
                .description("adipisicing")
                .releaseDate("1967-03-25")
                .duration(100)
                .build();
        assertThrows(ValidationException.class, () -> filmController.saveFilm(film));
    }

    @Test
    void descriptionLengthCanNotBeMore200Chars() {
        Film film = Film.builder()
                .name("nisi eiusmod")
                .description("adipisicingadipisicingadipisicingadipisicingadipisicingadipisicingadipisic" +
                        "ingadipisicingadipisicingadipisicingadipisicingadipisicingadipisicingadipisicingadipisici" +
                        "ngadipisicingadipisicingadipisicingadipisicingadipisicing")
                .releaseDate("1967-03-25")
                .duration(100)
                .build();
        assertThrows(ValidationException.class, () -> filmController.saveFilm(film));
    }

    @Test
    void releaseDateCanNotBeBefore28dec1895() {
        Film film = Film.builder()
                .name("nisi eiusmod")
                .description("adipisicing")
                .releaseDate("1895-12-27")
                .duration(100)
                .build();
        assertThrows(ValidationException.class, () -> filmController.saveFilm(film));
    }

    @Test
    void durationCanNotBeLessThanZero() {
        Film film = Film.builder()
                .name("nisi eiusmod")
                .description("adipisicing")
                .releaseDate("1967-03-25")
                .duration(-1)
                .build();
        assertThrows(ValidationException.class, () -> filmController.saveFilm(film));
    }
}