package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.dto.MpaRatingDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcMpaRepository extends BaseRepository<MpaRatingDto> implements MpaRepository {
    public JdbcMpaRepository(NamedParameterJdbcTemplate jdbc) {
        super(jdbc, MpaRatingDto.class);
    }


    @Override
    public Optional<MpaRatingDto> getById(int id) {
        String query = "SELECT * FROM RATINGS WHERE RATING_ID = :id";
        return findOne(query, Map.of(
                "id", id
        ), new MpaRowMapper());
    }

    @Override
    public List<MpaRatingDto> getAll() {
        String query = "SELECT * FROM RATINGS";
        return findMany(query, Map.of(), new MpaRowMapper());
    }
}
