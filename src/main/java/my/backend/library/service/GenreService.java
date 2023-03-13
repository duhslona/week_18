package my.backend.library.service;


import my.backend.library.dto.GenreDto;

public interface GenreService {

    GenreDto getGenreById(Long id);
}
