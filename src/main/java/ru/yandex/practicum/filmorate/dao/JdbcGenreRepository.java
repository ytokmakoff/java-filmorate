package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.mappers.GenreDtoRowMapper;
import ru.yandex.practicum.filmorate.dto.GenreDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcGenreRepository extends BaseRepository<GenreDto> implements GenreRepository {
    public JdbcGenreRepository(NamedParameterJdbcTemplate jdbc) {
        super(jdbc, GenreDto.class);
    }

    @Override
    public Optional<GenreDto> getById(int id) {
        String query = "SELECT * FROM GENRES WHERE GENRE_ID = :genreId";
        return findOne(query, Map.of("genreId", id), new GenreDtoRowMapper());
    }

    @Override
    public List<GenreDto> findAll() {
        String query = "SELECT * FROM GENRES";
        return findMany(query, Map.of(), new GenreDtoRowMapper());
    }
}
