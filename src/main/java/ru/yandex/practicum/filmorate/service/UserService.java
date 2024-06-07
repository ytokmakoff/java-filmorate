package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.dao.JdbcUserRepository;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final JdbcUserRepository jdbcUserRepository;

    public User createUser(@RequestBody User user) throws ValidationException {
        validateUser(user);
        log.info("Validated user: {}", user);
        jdbcUserRepository.create(user);
        log.info("Saved user: {}", user);
        return user;
    }

    public User updateUser(@RequestBody User user) throws ValidationException {
        if (jdbcUserRepository.getById(user.getId()).isEmpty()) {
            log.warn("User not found: {}", user);
            throw new UserNotFoundException("User not found");
        }
        validateUser(user);
        log.info("Validated user: {}", user);
        jdbcUserRepository.update(user);
        log.info("Updated user: {}", user);
        return user;
    }

    public User getUserById(int id) {
        if (jdbcUserRepository.getById(id).isEmpty()) {
            log.warn("User with id: {} not found", id);
            throw new UserNotFoundException("User not found");
        }
        User user = jdbcUserRepository.getById(id).get();
        log.info("Retrieved user by id: {}", id);
        return user;
    }

    public List<User> allUsers() {
        List<User> users = jdbcUserRepository.getAll();
        log.info("Retrieved add users total: {}", users.size());
        return users;
    }

    public void addFriend(int id, int friendId) {
        if (jdbcUserRepository.getById(id).isEmpty()) {
            log.warn("User with id: {} not found", id);
            throw new UserNotFoundException("User not found");
        }
        if (jdbcUserRepository.getById(friendId).isEmpty()) {
            log.warn("User with id: {} not found", friendId);
            throw new UserNotFoundException("User not found");
        }
        jdbcUserRepository.addFriend(id, friendId);
    }

    public void removeFriend(int id, int friendId) {
        if (jdbcUserRepository.getById(id).isEmpty()) {
            log.warn("User with id: {} not found", id);
            throw new UserNotFoundException("User not found");
        }
        if (jdbcUserRepository.getById(friendId).isEmpty()) {
            log.warn("User with id: {} not found", friendId);
            throw new UserNotFoundException("User not found");
        }
        jdbcUserRepository.deleteFriend(id, friendId);
    }

    public List<User> userFriends(int id) {
        if (jdbcUserRepository.getById(id).isEmpty()) {
            log.warn("User with id: {} not found", id);
            throw new UserNotFoundException("User not found");
        }
        List<User> friends = jdbcUserRepository.getFriends(id);
        log.info("Retrieved user with id: {} friends total: {}", id, friends.size());
        return friends;
    }

    public List<User> mutualFriends(int id, int friendId) {
        if (jdbcUserRepository.getById(id).isEmpty()) {
            log.warn("User with id {} not found", id);
            throw new UserNotFoundException("User not found");
        }
        if (jdbcUserRepository.getById(friendId).isEmpty()) {
            log.warn("User with id {} not found", id);
            throw new UserNotFoundException("User not found");
        }
        List<User> mutualFriends = jdbcUserRepository.mutualFriends(id, friendId);
        log.info("Retrieved mutualFriends between user userId: {} and friendId {} total: {}", id, friendId, mutualFriends.size());
        return mutualFriends;
    }

    private boolean validateUser(User user) throws ValidationException {
        if (user.getEmail().isBlank()) {
            log.warn("Validation failed: User email is blank");
            throw new ValidationException("User email is blank");
        }
        if (!user.getEmail().contains("@")) {
            log.warn("Validation failed: User email: {} doesn't contains @", user.getEmail());
            throw new ValidationException("User email doesn't contains @");
        }
        if (user.getLogin().isBlank()) {
            log.warn("Validation failed: User login is blank");
            throw new ValidationException("User login is blank");
        }
        if (user.getLogin().contains(" ")) {
            log.warn("Validation failed: User login: {} contains spaces", user.getLogin());
            throw new ValidationException("User login contains spaces");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("User birthday: {} is after now", user.getBirthday());
            throw new ValidationException("User birthday is after now");
        }
        return true;
    }
}