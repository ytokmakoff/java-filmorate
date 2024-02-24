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
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private int generateId = 1;
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public User saveUser(@RequestBody User user) throws ValidationException {
        validateUser(user);
        user.setId(generateId());
        inMemoryUserStorage.saveUser(user);
        return user;
    }

    public User updateExistingUser(@RequestBody User user) throws ValidationException {
        if (inMemoryUserStorage.getUserById(user.getId()) == null) {
            throw new UserNotFoundException("User not found");
        }
        validateUser(user);
        inMemoryUserStorage.updateExistingUser(user);
        return user;
    }
    public User userById(int id) {
        if (inMemoryUserStorage.getUserById(id) == null) {
            throw new UserNotFoundException("User not found");
        }
        return inMemoryUserStorage.getUserById(id);
    }

    public List<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }

    public void addFriend(int id, int friendId) {
        if (inMemoryUserStorage.getUserById(id) != null && inMemoryUserStorage.getUserById(friendId) != null) {
            inMemoryUserStorage.getUserById(id).getFriends().add(friendId);
            inMemoryUserStorage.getUserById(friendId).getFriends().add(id);
            log.info("friends added");
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    public void removeFriend(int id, int friendId) {
        if (inMemoryUserStorage.getUserById(id) != null && inMemoryUserStorage.getUserById(friendId) != null) {
            inMemoryUserStorage.getUserById(id).getFriends().remove(friendId);
            inMemoryUserStorage.getUserById(friendId).getFriends().remove(id);
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    public Set<Integer> userFriends(int id) {
        return inMemoryUserStorage.getUserById(id).getFriends();
    }

    public List<Integer> mutualFriends(int id, int otherId) {
        if (inMemoryUserStorage.getUserById(id) == null || inMemoryUserStorage.getUserById(otherId) == null) {
            throw new UserNotFoundException("user not found");
        }
        return inMemoryUserStorage.getUserById(id).getFriends().stream()
                .filter(u -> (inMemoryUserStorage.getUserById(otherId)).getFriends().contains(u))
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
