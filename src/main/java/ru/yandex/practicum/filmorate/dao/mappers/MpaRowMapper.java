package ru.yandex.practicum.filmorate.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.MpaRatingDto;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MpaRowMapper implements RowMapper<MpaRatingDto> {
    @Override
    public MpaRatingDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        MpaRatingDto mpa = new MpaRatingDto();
        int ratingId = rs.getInt("rating_id");
        mpa.setId(ratingId);
        mpa.setName(MpaRating.ratingIdToStringName(ratingId));
        return mpa;
    }
}
