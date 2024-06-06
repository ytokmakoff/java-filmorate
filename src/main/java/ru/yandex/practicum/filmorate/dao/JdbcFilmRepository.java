package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.mappers.GenreDtoRowMapper;
import ru.yandex.practicum.filmorate.dao.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dao.mappers.LikeDtoRowMapper;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.LikeDto;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Repository
public class JdbcFilmRepository extends BaseRepository<Film> implements FilmRepository {
    public JdbcFilmRepository(NamedParameterJdbcTemplate jdbc) {
        super(jdbc, Film.class);
    }

    @Override
    public Optional<Film> getById(long id) {
        String query = """
                SELECT *
                FROM FILMS AS F
                LEFT JOIN RATINGS AS R ON F.RATING_ID = R.RATING_ID
                WHERE F.FILM_ID = :filmId
                """;
        Optional<Film> film = findOne(query, Map.of("filmId", id), new FilmRowMapper());
        if (film.isEmpty())
            throw new FilmNotFoundException("film with id: " + id + " not found");
        if (film.get().getGenres() != null) {
            String genreQuery = "SELECT g.genre_id, g.name " +
                                "FROM genres g " +
                                "JOIN films_genres fg ON g.genre_id = fg.genre_id " +
                                "WHERE fg.film_id = :filmId";
            List<GenreDto> genres = findMany(genreQuery, Map.of("filmId", id), new GenreDtoRowMapper());
            film.get().setGenres(genres);
        }

        String likeQuery = """
                SELECT L.USER_ID, L.FILM_ID
                FROM LIKES AS L
                JOIN FILMS F ON L.FILM_ID = F.FILM_ID
                WHERE F.FILM_ID = :filmId
                """;
        List<LikeDto> likes = findMany(likeQuery, Map.of("filmId", id), new LikeDtoRowMapper());
        film.get().setLikes(likes);
        return film;
    }

    @Override
    public List<Film> getAll() {
        String query = """
                SELECT F.*, R.rating_id, R.rating, g.genre_id, g.name
                FROM FILMS AS F
                LEFT JOIN RATINGS AS R ON F.RATING_ID = R.RATING_ID
                LEFT JOIN films_genres fg ON F.FILM_ID = fg.film_id
                LEFT JOIN genres g ON fg.genre_id = g.genre_id
                """;

        List<Film> films = findMany(query, Map.of(), new FilmRowMapper());

        for (Film film : films) {
            String genreQuery = """
                    SELECT g.genre_id, g.name
                    FROM genres g
                    JOIN films_genres fg ON g.genre_id = fg.genre_id
                    WHERE fg.film_id = :filmId
                    """;
            List<GenreDto> genres = findMany(genreQuery, Map.of("filmId", film.getId()), new GenreDtoRowMapper());
            film.setGenres(genres);

            String likeQuery = """
                    SELECT L.USER_ID, L.FILM_ID
                    FROM LIKES AS L
                    WHERE L.FILM_ID = :filmId
                    """;
            List<LikeDto> likes = findMany(likeQuery, Map.of("filmId", film.getId()), new LikeDtoRowMapper());
            film.setLikes(likes);
        }

        return films;
    }


    @Override
    public Film create(Film film) {
        String query;
        Map<String, Object> params = new HashMap<>();
        params.put("name", film.getName());
        params.put("description", film.getDescription());
        params.put("releaseDate", film.getReleaseDate());
        params.put("duration", film.getDuration());

        if (film.getMpa() == null) {
            query = "INSERT INTO FILMS(NAME, DESCRIPTION, RELEASE_DATE, DURATION) " +
                    "VALUES (:name, :description, :releaseDate, :duration)";
        } else {
            params.put("ratingId", film.getMpa().getId());
            query = "INSERT INTO FILMS(name, description, release_date, duration, rating_id) " +
                    "SELECT :name, :description, :releaseDate, :duration, :ratingId";
        }
        long id = insert(query, params, "film_id");
        film.setId(id);

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            film.setGenres(film.getGenres().stream().distinct().toList());
            for (GenreDto genre : film.getGenres()) {
                String genreQuery = "INSERT INTO films_genres(film_id, genre_id) " +
                                    "VALUES (:filmId, :genreId)";

                jdbc.update(genreQuery, Map.of(
                        "filmId", id,
                        "genreId", genre.getId()
                ));
            }
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        String query = """
                UPDATE FILMS
                SET name = :name,
                    description = :description,
                    release_date = :releaseDate,
                    duration = :duration,
                    rating_id = :ratingId
                WHERE film_id = :filmId
                """;

        Map<String, Object> params = new HashMap<>();
        params.put("filmId", film.getId());
        params.put("name", film.getName());
        params.put("description", film.getDescription());
        params.put("releaseDate", film.getReleaseDate());
        params.put("duration", film.getDuration());
        params.put("ratingId", film.getMpa() != null ? film.getMpa().getId() : null);

        jdbc.update(query, params);

        String deleteGenresQuery = "DELETE FROM films_genres WHERE film_id = :filmId";
        jdbc.update(deleteGenresQuery, Map.of("filmId", film.getId()));

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            film.setGenres(film.getGenres().stream().distinct().toList());
            for (GenreDto genre : film.getGenres()) {
                String genreQuery = "INSERT INTO films_genres(film_id, genre_id) " +
                                    "VALUES (:filmId, :genreId)";

                jdbc.update(genreQuery, Map.of(
                        "filmId", film.getId(),
                        "genreId", genre.getId()
                ));
            }
        }

        return film;
    }

    @Override
    public void addLike(long filmId, long userId) {
        String query = """
                INSERT INTO LIKES(USER_ID, FILM_ID)
                VALUES (:userId, :filmId)
                """;

        insert(query, Map.of(
                "userId", userId,
                "filmId", filmId
        ), "like_id");
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        String query = """
                DELETE FROM LIKES
                WHERE FILM_ID = :filmId AND USER_ID = :userId
                """;
        delete(query, Map.of(
                "filmId", filmId,
                "userId", userId
        ));
    }

    @Override
    public List<Film> getPopular(int count) {
        String query = """
                SELECT F.*, R.rating, COUNT(L.like_id) AS like_count
                FROM films AS F
                LEFT JOIN likes AS L ON F.film_id = L.film_id
                LEFT JOIN ratings AS R ON F.rating_id = R.rating_id
                GROUP BY F.film_id, R.rating
                ORDER BY like_count DESC
                LIMIT :count;
                """;

        return findMany(query, Map.of(
                "count", count
        ), new FilmRowMapper());
    }
}
