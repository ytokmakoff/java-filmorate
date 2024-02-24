package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User saveUser(@RequestBody User user) throws ValidationException {
        return userService.saveUser(user);
    }

    @PutMapping
    public User updateExistingUser(@RequestBody User user) throws ValidationException {
        return userService.updateExistingUser(user);
    }

    @PutMapping("{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void removeFriend(int id, int friendId) {
        userService.removeFriend(id, friendId);
    }

    @GetMapping("{id}/friends")
    public Set<Integer> userFriends(@PathVariable int id) {
        return userService.userFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<Integer> mutualFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.mutualFriends(id, otherId);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}