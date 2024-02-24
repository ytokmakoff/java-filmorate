package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User saveUser(User user);
    User updateExistingUser(User user) throws ValidationException;
    List<User> getAllUsers();
}
