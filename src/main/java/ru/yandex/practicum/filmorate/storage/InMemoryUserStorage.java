package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> userMap = new HashMap<>();

    @Override
    public User saveUser(User user) {
        userMap.put(user.getId(), user);
        log.info("User saved: {}", user);
        return user;
    }

    @Override
    public User updateExistingUser(User user) throws ValidationException {
        if (userMap.containsKey(user.getId())) {
            userMap.put(user.getId(), user);
            log.info("User with id {} updated", user.getId());
        } else {
            log.warn("User with id {} not exist", user.getId());
            throw new ValidationException("User id not exist");
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Retrieving all users");
        return new ArrayList<>(userMap.values());
    }

    public User getUserById(int id) {
        return userMap.get(id);
    }
}
