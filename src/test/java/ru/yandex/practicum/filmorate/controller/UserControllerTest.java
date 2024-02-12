package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {
    UserController userController;

    @BeforeEach
    void init() {
        userController = new UserController();
    }

    @Test
    void createUserCorrect() throws ValidationException {
        User user = User.builder()
                .login("dolore")
                .name("Nick Name")
                .email("mail@mail.ru")
                .birthday("1946-08-20")
                .build();
        userController.saveUser(user);
        assertEquals(1, userController.getAllUsers().size());
        assertEquals(user, userController.getAllUsers().get(0));
    }

    @Test
    void updateExistingUserCorrect() throws ValidationException {
        User user = User.builder()
                .login("dolore")
                .name("Nick Name")
                .email("mail@mail.ru")
                .birthday("1946-08-20")
                .build();
        userController.saveUser(user);
        User user2 = User.builder()
                .login("newUser")
                .name("name name name")
                .email("mailmail@mail.ru")
                .birthday("1999-08-20")
                .build();
        user2.setId(user.getId());
        userController.updateExistingUser(user2);
        assertEquals(1, userController.getAllUsers().size());
        assertEquals(user2, userController.getAllUsers().get(0));
    }

    @Test
    void updateExistingUserIncorrectId() throws ValidationException {
        User user = User.builder()
                .login("dolore")
                .name("Nick Name")
                .email("mail@mail.ru")
                .birthday("1946-08-20")
                .build();
        userController.saveUser(user);
        User user2 = User.builder()
                .login("newUser")
                .name("name name name")
                .email("mailmail@mail.ru")
                .birthday("1999-08-20")
                .build();
        user2.setId(100);
        assertThrows(ValidationException.class, () -> userController.updateExistingUser(user2));
    }

    @Test
    void emailCanNotBeBlank() {
        User user = User.builder()
                .login("dolore")
                .name("Nick Name")
                .email("")
                .birthday("1946-08-20")
                .build();
        assertThrows(ValidationException.class, () -> userController.saveUser(user));
    }

    @Test
    void emailShouldContainsAt() {
        User user = User.builder()
                .login("dolore")
                .name("Nick Name")
                .email("mailmail.ru")
                .birthday("1946-08-20")
                .build();
        assertThrows(ValidationException.class, () -> userController.saveUser(user));
    }

    @Test
    void loginCannotBeEmpty() {
        User user = User.builder()
                .login("")
                .name("Nick Name")
                .email("mail@mail.ru")
                .birthday("1946-08-20")
                .build();
        assertThrows(ValidationException.class, () -> userController.saveUser(user));
    }

    @Test
    void loginCannotContainsSpaces() {
        User user = User.builder()
                .login("dolore  ")
                .name("Nick Name")
                .email("mail@mail.ru")
                .birthday("1946-08-20")
                .build();

        assertThrows(ValidationException.class, () -> userController.saveUser(user));
    }

    @Test
    void birthdayDateCannotBeInFuture() {
        User user = User.builder()
                .login("dolore")
                .name("Nick Name")
                .email("mail@mail.ru")
                .birthday("2045-08-20")
                .build();

        assertThrows(ValidationException.class, () -> userController.saveUser(user));
    }

    @Test
    void nameCanBeEmptyThisLoginWillBeUsed() throws ValidationException {
        User user = User.builder()
                .login("dolore")
                .name("")
                .email("mail@mail.ru")
                .birthday("1946-08-20")
                .build();
        userController.saveUser(user);
        assertEquals(user.getLogin(), user.getName());
    }
}