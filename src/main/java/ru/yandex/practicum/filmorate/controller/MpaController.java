package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.MpaRatingDto;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("mpa")
public class MpaController {
    private final MpaService mpaService;

    @GetMapping("{id}")
    public MpaRatingDto getById(@PathVariable int id) {
        return mpaService.getById(id);
    }

    @GetMapping
    public List<MpaRatingDto> getAll() {
        return mpaService.getAll();
    }
}
