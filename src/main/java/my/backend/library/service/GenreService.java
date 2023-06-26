package my.backend.library.service;


import my.backend.library.dto.GenreWithBooksDto;

public interface GenreService {

    GenreWithBooksDto getGenreById(Long id);

}
