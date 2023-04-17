package my.backend.library.controller;

import lombok.RequiredArgsConstructor;
import my.backend.library.dto.GenreDto;
import my.backend.library.service.GenreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/genre/{id}")
    List<GenreDto> getGenreById(@PathVariable("id") Long id) {
        return genreService.getGenreById(id);
    }
}
