package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcUserRepository extends BaseRepository<User> implements UserRepository {
    public JdbcUserRepository(NamedParameterJdbcTemplate jdbc) {
        super(jdbc, User.class);
    }

    @Override
    public Optional<User> getById(long id) {
        String query = "SELECT * FROM users WHERE user_id = :id";
        return findOne(query, Map.of("id", id), new UserRowMapper());
    }

    @Override
    public List<User> getAll() {
        String query = "SELECT * FROM users";
        return findMany(query, Map.of(), new UserRowMapper());
    }

    @Override
    public User create(User user) {
        String query = "INSERT INTO users(name, email, login, birthday) VALUES(:name, :email, :login, :birthday)";

        long id = insert(query,
                Map.of("name", user.getName(),
                        "email", user.getEmail(),
                        "login", user.getLogin(),
                        "birthday", user.getBirthday()
                ), "user_id");
        user.setId(id);
        return user;
    }

    @Override
    public User update(User user) {
        String query = """
                UPDATE users
                SET name = :name, email = :email, login = :login, birthday = :birthday
                WHERE user_id = :id
                """;
        update(query, Map.of(
                "name", user.getName(),
                "email", user.getEmail(),
                "login", user.getLogin(),
                "birthday", user.getBirthday(),
                "id", user.getId()
        ));
        return user;
    }

    public List<User> getFriends(long userId) {
        String query = """
                SELECT u.USER_ID,
                       u.NAME,
                       u.EMAIL,
                       u.LOGIN,
                       u.BIRTHDAY
                FROM USERS AS u
                JOIN FRIENDSHIP AS f ON u.USER_ID = f.FRIEND_ID
                WHERE f.USER_ID = :userId;
                """;
        return findMany(query, Map.of(
                "userId", userId
        ), new UserRowMapper());
    }

    @Override
    public void addFriend(long userId, long friendId) {
        String query = """
                INSERT INTO FRIENDSHIP(user_id, friend_id)
                VALUES (:userId, :friendId)
                """;
        update(query, Map.of(
                "userId", userId,
                "friendId", friendId)
        );
    }

    public List<User> commonFriend(int id, int otherId) {
        String query = """
                SELECT u.USER_ID, u.NAME, u.EMAIL, u.LOGIN, u.BIRTHDAY
                FROM USERS AS u
                JOIN FRIENDSHIP AS f1 ON f1.FRIEND_ID = u.USER_ID
                JOIN FRIENDSHIP AS f2 ON f2.FRIEND_ID = u.USER_ID
                WHERE f1.USER_ID = :id AND f2.USER_ID = :otherId
                """;
        return findMany(query, Map.of(
                "id", id,
                "otherId", otherId), new UserRowMapper()
        );
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        String query = "DELETE FROM FRIENDSHIP WHERE USER_ID = :userId AND FRIEND_ID = :friendId";

        delete(query, Map.of(
                "userId", userId,
                "friendId", friendId
        ));
    }
}