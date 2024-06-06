package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcUserRepository.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcUserRepositoryTest {
    static final int USER1_ID = 1;
    static final int USER2_ID = 2;
    static final int USER3_ID = 3;

    static final int NEW_USER_ID = 4;
    final JdbcUserRepository jdbc;

    static User getTestUser1() {
        User user = new User();
        user.setId(USER1_ID);
        user.setName("name");
        user.setEmail("email");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        return user;
    }

    static User getTestUser2() {
        User user = new User();
        user.setId(USER2_ID);
        user.setName("name2");
        user.setEmail("email2");
        user.setLogin("login2");
        user.setBirthday(LocalDate.of(2045, 1, 1));
        return user;
    }

    static User getTestUser3() {
        User user = new User();
        user.setId(USER3_ID);
        user.setName("name3");
        user.setEmail("email");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2012, 12, 2));
        return user;
    }

    @Test
    void getByIdTest() {
        assertThat(jdbc.getById(USER1_ID))
                .isPresent()
                .get()
                .isEqualTo(getTestUser1());
    }

    @Test
    void getAllTest() {
        assertThat(jdbc.getAll())
                .usingRecursiveComparison()
                .isEqualTo(List.of(getTestUser1(), getTestUser2(), getTestUser3()));
    }

    @Test
    void createTest() {
        User user = new User();
        user.setId(NEW_USER_ID);
        user.setName("new user name");
        user.setLogin("looogin");
        user.setEmail("eeee");
        user.setBirthday(LocalDate.of(2075, 1, 1));

        jdbc.create(user);
        assertThat(jdbc.getById(NEW_USER_ID))
                .isPresent()
                .get()
                .isEqualTo(user);
    }

    @Test
    void updateTest() {
        User newUser = new User();
        newUser.setId(USER1_ID);
        newUser.setEmail("email");
        newUser.setLogin("new login");
        newUser.setName("new name");
        newUser.setBirthday(LocalDate.of(2010, 12, 20));
        jdbc.update(newUser);
        assertThat(jdbc.getById(USER1_ID))
                .isPresent()
                .get()
                .isEqualTo(newUser);
    }

    @Test
    void friendsTest() {
        jdbc.addFriend(USER1_ID, USER3_ID);
        assertThat(jdbc.getFriends(USER1_ID))
                .isEqualTo(List.of(getTestUser3()));

        jdbc.addFriend(USER2_ID, USER3_ID);

        assertThat(jdbc.commonFriend(USER1_ID, USER2_ID))
                .isEqualTo(List.of(getTestUser3()));

        jdbc.deleteFriend(USER1_ID, USER3_ID);

        assertThat(jdbc.getFriends(USER1_ID))
                .isEqualTo(List.of());
    }


}