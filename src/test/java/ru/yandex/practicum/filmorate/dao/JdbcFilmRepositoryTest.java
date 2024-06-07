package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dto.LikeDto;
import ru.yandex.practicum.filmorate.dto.MpaRatingDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcFilmRepository.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcFilmRepositoryTest {
    static final int TEST_FILM1_ID = 1;
    static final int TEST_FILM2_ID = 2;
    static final int NEW_FILM_ID = 3;
    static final int TEST_USER_ID = 1;
    static final int TEST_MPA_ID = 2;
    final JdbcFilmRepository jdbc;

    static Film getTestFilm1() {
        Film film = new Film();

        film.setId(TEST_FILM1_ID);
        film.setName("test name");
        film.setDescription("test description");
        film.setReleaseDate(LocalDate.of(2000, 10, 9));
        film.setDuration(100);

        return film;
    }

    static Film getTestFilm2() {
        Film film = new Film();

        film.setId(TEST_FILM2_ID);
        film.setName("test name2");
        film.setDescription("test description2");
        film.setReleaseDate(LocalDate.of(1923, 8, 10));
        film.setDuration(100);

        MpaRatingDto mpa = new MpaRatingDto();
        mpa.setId(TEST_MPA_ID);
        mpa.setName(MpaRating.ratingIdToStringName(TEST_MPA_ID));

        film.setMpa(mpa);

        return film;
    }

    @Test
    void getByIdShouldReturnFilmById() {
        assertThat(jdbc.getById(TEST_FILM1_ID))
                .isPresent()
                .usingRecursiveComparison()
                .isEqualTo(Optional.of(getTestFilm1()));
    }

    @Test
    void getAllShouldReturnAllFilms() {
        List<Film> films = jdbc.getAll();
        assertThat(films)
                .usingRecursiveComparison()
                .isEqualTo(List.of(getTestFilm1(), getTestFilm2()));
    }

    @Test
    void updateShouldUpdateFilm() {
        Film newFilm = new Film();

        newFilm.setId(TEST_FILM1_ID);
        newFilm.setName("new name");
        newFilm.setDescription("new description");
        newFilm.setReleaseDate(LocalDate.of(2000, 10, 9));
        newFilm.setDuration(100);

        assertThat(jdbc.update(newFilm))
                .usingRecursiveComparison()
                .isEqualTo(newFilm);
    }

    @Test
    void addLikeShouldAddLikeToFilm() {
        jdbc.addLike(TEST_FILM1_ID, TEST_USER_ID);

        Film expectedFilm = getTestFilm1();
        LikeDto likeDto = new LikeDto();
        likeDto.setUserId(TEST_USER_ID);
        likeDto.setFilmId(TEST_FILM1_ID);
        expectedFilm.setLikes(List.of(likeDto));

        assertThat(jdbc.getById(TEST_FILM1_ID))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedFilm);
    }

    @Test
    void deleteLikeShouldRemoveLikeFromFilm() {
        jdbc.addLike(TEST_FILM1_ID, TEST_USER_ID);
        jdbc.deleteLike(TEST_FILM1_ID, TEST_USER_ID);
        Film expectedFilm = getTestFilm1();

        assertThat(jdbc.getById(TEST_FILM1_ID))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedFilm);
    }

    @Test
    void getPopularShouldReturnPopularFilms() {
        jdbc.addLike(TEST_FILM2_ID, TEST_USER_ID);

        assertThat(jdbc.getPopular(2))
                .isEqualTo(List.of(getTestFilm2(), getTestFilm1()));
    }

    @Test
    void createShouldCreateNewFilm() {
        Film film = new Film();
        film.setName("film");
        film.setDescription("desc");
        film.setReleaseDate(LocalDate.of(1999, 5, 5));
        film.setDuration(100);

        jdbc.create(film);

        assertThat(film)
                .usingRecursiveComparison()
                .isEqualTo(jdbc.getById(NEW_FILM_ID).get());
    }
}