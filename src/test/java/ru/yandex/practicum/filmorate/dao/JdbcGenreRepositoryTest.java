package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcGenreRepository.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcGenreRepositoryTest {
    final static int TEST_ID = 1;
    final JdbcGenreRepository jdbc;

    static GenreDto getTestGenre() {
        GenreDto genreDto = new GenreDto();
        genreDto.setId(TEST_ID);
        genreDto.setName(FilmGenre.filmGenreIdToString(TEST_ID));
        return genreDto;
    }

    static List<GenreDto> getAllTestGenre() {
        List<GenreDto> genreDtoList = new ArrayList<>();

        for (int i = 1; i <= 6; i++) {
            GenreDto genreDto = new GenreDto();
            genreDto.setId(i);
            genreDto.setName(FilmGenre.filmGenreIdToString(i));
            genreDtoList.add(genreDto);
        }
        return genreDtoList;
    }

    @Test
    void getByIdTest() {
        Optional<GenreDto> genreDto = jdbc.getById(TEST_ID);
        assertThat(genreDto)
                .isPresent()
                .get()
                .isEqualTo(getTestGenre());
    }

    @Test
    void findAllTest() {
        List<GenreDto> genreDtoList = jdbc.findAll();
        assertThat(genreDtoList)
                .isEqualTo(getAllTestGenre());
    }
}