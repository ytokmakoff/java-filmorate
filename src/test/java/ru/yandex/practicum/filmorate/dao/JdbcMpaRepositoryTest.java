package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dto.MpaRatingDto;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.ArrayList;
import java.util.List;

@JdbcTest
@Import(JdbcMpaRepository.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcMpaRepositoryTest {
    static final int TEST_ID = 1;
    final JdbcMpaRepository jdbc;

    static MpaRatingDto getTestMpa() {
        MpaRatingDto mpa = new MpaRatingDto();
        mpa.setId(TEST_ID);
        mpa.setName(MpaRating.ratingIdToStringName(TEST_ID));
        return mpa;
    }

    static List<MpaRatingDto> getAllTestMpa() {
        List<MpaRatingDto> mpaRatingDtoList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            MpaRatingDto mpa = new MpaRatingDto();
            mpa.setId(i);
            mpa.setName(MpaRating.ratingIdToStringName(i));
            mpaRatingDtoList.add(mpa);
        }
        return mpaRatingDtoList;
    }

    @Test
    public void getByIdTest() {
        assertThat(jdbc.getById(TEST_ID))
                .isPresent()
                .get()
                .isEqualTo(getTestMpa());
    }

    @Test
    public void findAllTest() {
        assertThat(jdbc.getAll())
                .isEqualTo(getAllTestMpa());
    }
}