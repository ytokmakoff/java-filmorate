package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final InMemoryUserStorage inMemoryUserStorage;
    private int generateId = 1;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public User saveUser(@RequestBody User user) throws ValidationException {
        validateUser(user);
        user.setId(generateId());
        inMemoryUserStorage.saveUser(user);
        log.info("user: {} saved", user);
        return user;
    }

    public User updateExistingUser(@RequestBody User user) throws ValidationException {
        if (inMemoryUserStorage.getUserById(user.getId()) == null) {
            log.warn("user: {} not found", user);
            throw new UserNotFoundException("User not found");
        }
        validateUser(user);
        inMemoryUserStorage.updateExistingUser(user);
        log.info("user: {} updated", user);
        return user;
    }

    public User userById(int id) {
        if (inMemoryUserStorage.getUserById(id) == null) {
            log.warn("user with id: {} not found", id);
            throw new UserNotFoundException("User not found");
        }
        log.info("user by id: {} received", id);
        return inMemoryUserStorage.getUserById(id);
    }

    public List<User> getAllUsers() {
        log.info("got all users");
        return inMemoryUserStorage.getAllUsers();
    }

    public void addFriend(int id, int friendId) {
        if (inMemoryUserStorage.getUserById(id) != null && inMemoryUserStorage.getUserById(friendId) != null) {
            inMemoryUserStorage.getUserById(id).getFriends().add(friendId);
            inMemoryUserStorage.getUserById(friendId).getFriends().add(id);
            log.info("friends added");
        } else {
            log.warn("user with id {} or {} not found", id, friendId);
            throw new UserNotFoundException("User not found");
        }
    }

    public void removeFriend(int id, int friendId) {
        if (inMemoryUserStorage.getUserById(id) != null && inMemoryUserStorage.getUserById(friendId) != null) {
            inMemoryUserStorage.getUserById(id).getFriends().remove(friendId);
            inMemoryUserStorage.getUserById(friendId).getFriends().remove(id);
        } else {
            log.warn("user with id {} or {} not found", id, friendId);
            throw new UserNotFoundException("User not found");
        }
    }

    public List<User> userFriends(int id) {
        if (inMemoryUserStorage.getUserById(id) == null) {
            log.warn("user with id: {} not found", id);
            throw new UserNotFoundException("User not found");
        }
        log.info("received user: {} friend", id);
        return inMemoryUserStorage.getUserById(id).getFriends().stream()
                .map(inMemoryUserStorage::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> mutualFriends(int id, int otherId) {
        if (inMemoryUserStorage.getUserById(id) == null || inMemoryUserStorage.getUserById(otherId) == null) {
            log.warn("user with id {} or {} not found", id, otherId);
            throw new UserNotFoundException("user not found");
        }
        log.info("received mutualFriends between two users with ids {} and {}", id, otherId);
        return inMemoryUserStorage.getUserById(id).getFriends().stream()
                .filter(u -> (inMemoryUserStorage.getUserById(otherId)).getFriends().contains(u))
                .map(inMemoryUserStorage::getUserById)
                .collect(Collectors.toList());
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (LocalDate.parse(user.getBirthday(), formatter).isAfter(LocalDate.now())) {
            log.warn("user birthday: {} is after now", user.getBirthday());
            throw new ValidationException("user birthday is after now");
        }
        return true;
    }

    private int generateId() {
        return generateId++;
    }
}
