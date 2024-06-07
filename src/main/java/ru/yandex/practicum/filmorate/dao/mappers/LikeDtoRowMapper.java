package ru.yandex.practicum.filmorate.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.LikeDto;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LikeDtoRowMapper implements RowMapper<LikeDto> {
    @Override
    public LikeDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        LikeDto like = new LikeDto();
        like.setFilmId(rs.getInt("film_id"));
        like.setUserId(rs.getInt("user_id"));
        return like;
    }
}
