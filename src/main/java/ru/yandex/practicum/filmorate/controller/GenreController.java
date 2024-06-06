package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("genres")
public class GenreController {
    private final GenreService genreService;

    @GetMapping("{id}")
    public GenreDto getById(@PathVariable int id) {
        return genreService.getById(id);
    }

    @GetMapping
    public List<GenreDto> findAll() {
        return genreService.findAll();
    }
}
