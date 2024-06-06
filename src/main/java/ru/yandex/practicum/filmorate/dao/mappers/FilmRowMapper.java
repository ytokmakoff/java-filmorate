package ru.yandex.practicum.filmorate.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.MpaRatingDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("film_id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        int ratingId = rs.getInt("rating_id");
        if (rs.wasNull()) {
            film.setMpa(null);
        } else {
            MpaRatingDto mpaRating = new MpaRatingDto();
            mpaRating.setId(ratingId);
            mpaRating.setName(MpaRating.ratingIdToStringName(ratingId));
            film.setMpa(mpaRating);
        }
        return film;
    }
}