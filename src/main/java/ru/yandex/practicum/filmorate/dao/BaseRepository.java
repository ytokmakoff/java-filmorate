package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.yandex.practicum.filmorate.exception.InternalServerException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class BaseRepository<T> {
    protected final NamedParameterJdbcTemplate jdbc;
    private final Class<T> entityType;

    protected Optional<T> findOne(String query, Map<String, ?> paramMap, RowMapper<T> mapper) {
        try {
            T result = jdbc.queryForObject(query, paramMap, mapper);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    protected <E> List<E> findMany(String query, Map<String, ?> paramMap, RowMapper<E> mapper) {
        return jdbc.query(query, paramMap, mapper);
    }

    protected long insert(String query, Map<String, ?> paramMap, String keyColumnName) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(query,
                new MapSqlParameterSource(paramMap),
                keyHolder, new String[]{keyColumnName});

        Number id = keyHolder.getKey();
        if (id != null)
            return id.longValue();
        else {
            throw new InternalServerException("Не удалось сохранить данные");
        }
    }

    protected void update(String query, Map<String, ?> paramMap) {
        int rowsUpdated = jdbc.update(query, paramMap);
        if (rowsUpdated == 0)
            throw new InternalServerException("Не удалось обновить данные");
    }

    protected void delete(String query, Map<String, ?> paramMap) {
        jdbc.update(query, paramMap);
    }
}
