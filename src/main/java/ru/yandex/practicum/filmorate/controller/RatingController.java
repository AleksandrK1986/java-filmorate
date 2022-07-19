package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.Collection;

@RestController
public class RatingController {

    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @ResponseBody
    @GetMapping("/mpa")
    public Collection<RatingMPA> getAllMPA() {
        return ratingService.getAllMPA();
    }

    @ResponseBody
    @GetMapping("/mpa/{id}")
    public RatingMPA getMPAById(@PathVariable int id) {
        return ratingService.getMPAById(id);
    }

}
