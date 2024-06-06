package ru.yandex.practicum.filmorate.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreDtoRowMapper implements RowMapper<GenreDto> {
    @Override
    public GenreDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        GenreDto genreDto = new GenreDto();
        int genreId = rs.getInt("genre_id");
        genreDto.setId(genreId);
        genreDto.setName(FilmGenre.filmGenreIdToString(genreId));
        return genreDto;
    }
}
