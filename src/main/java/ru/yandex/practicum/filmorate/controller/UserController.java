package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private int generateId = 1;
    Map<Integer, User> userMap = new HashMap<>();

    @PostMapping
    User saveUser(@RequestBody User user) throws ValidationException {
        validateUser(user);
        user.setId(generateId());
        userMap.put(user.getId(), user);
        log.info("User created: {}", user);

        return user;
    }

    @PutMapping
    User updateExistingUser(@RequestBody User user) throws ValidationException {
        validateUser(user);
        if (userMap.containsKey(user.getId())) {
            userMap.put(user.getId(), user);
            log.info("User with id {} updated", user.getId());
        } else {
            log.warn("User with id {} not exist", user.getId());
            throw new ValidationException("User id not exist");
        }
        return user;
    }

    @GetMapping
    List<User> getAllUsers() {
        log.info("Retrieving all users");
        return new ArrayList<>(userMap.values());
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