package ru.yandex.practicum.filmorate.service;

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
public class UserService {
    private final JdbcUserRepository jdbcUserRepository;

    public UserService(JdbcUserRepository jdbcUserRepository) {
        this.jdbcUserRepository = jdbcUserRepository;
    }

    public User createUser(@RequestBody User user) throws ValidationException {
        validateUser(user);
        jdbcUserRepository.create(user);
        log.info("user: {} saved", user);
        return user;
    }

    public User updateUser(@RequestBody User user) throws ValidationException {
        if (jdbcUserRepository.getById(user.getId()).isEmpty()) {
            log.warn("user: {} not found", user);
            throw new UserNotFoundException("User not found");
        }
        validateUser(user);
        jdbcUserRepository.update(user);
        log.info("user: {} updated", user);
        return user;
    }

    public User getUserById(int id) {
        if (jdbcUserRepository.getById(id).isEmpty()) {
            log.warn("User with id: {} not found", id);
            throw new UserNotFoundException("User not found");
        }
        log.info("user by id: {} received", id);
        return jdbcUserRepository.getById(id).get();
    }

    public List<User> allUsers() {
        log.info("got all users");
        return jdbcUserRepository.getAll();
    }

    public void addFriend(int id, int friendId) {
        if (jdbcUserRepository.getById(id).isPresent() && jdbcUserRepository.getById(friendId).isPresent()) {
            jdbcUserRepository.addFriend(id, friendId);
        } else {
            log.warn("user with id {} or {} not found", id, friendId);
            throw new UserNotFoundException("User not found");
        }
    }

    public void removeFriend(int id, int friendId) {
        if (jdbcUserRepository.getById(id).isPresent() && jdbcUserRepository.getById(friendId).isPresent()) {
            jdbcUserRepository.deleteFriend(id, friendId);
        } else {
            log.warn("user with id {} or {} not found", id, friendId);
            throw new UserNotFoundException("User not found");
        }
    }

    public List<User> userFriends(int id) {
        if (jdbcUserRepository.getById(id).isEmpty()) {
            log.warn("user with id: {} not found", id);
            throw new UserNotFoundException("User not found");
        }
        log.info("received user: {} friend", id);
        return jdbcUserRepository.getFriends(id);
    }

    public List<User> commonFriends(int id, int friendId) {
        if (jdbcUserRepository.getById(id).isEmpty() && jdbcUserRepository.getById(friendId).isEmpty()) {
            log.warn("user with id {} or {} not found", id, friendId);
            throw new UserNotFoundException("user not found");
        }
        log.info("received mutualFriends between two users with ids {} and {}", id, friendId);
        return jdbcUserRepository.commonFriend(id, friendId);
    }

    private boolean validateUser(User user) throws ValidationException {
        if (user.getEmail().isBlank()) {
            log.warn("user email is blank");
            throw new ValidationException("user email is blank");
        }
        if (!user.getEmail().contains("@")) {
            log.warn("user email: {} not contains @", user.getEmail());
            throw new ValidationException("user email not contains @");
        }
        if (user.getLogin().isBlank()) {
            log.warn("user login is blank");
            throw new ValidationException("user login is blank");
        }
        if (user.getLogin().contains(" ")) {
            log.warn("user login: {} contains spaces", user.getLogin());
            throw new ValidationException("user login contains spaces");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("user birthday: {} is after now", user.getBirthday());
            throw new ValidationException("user birthday is after now");
        }
        return true;
    }
}