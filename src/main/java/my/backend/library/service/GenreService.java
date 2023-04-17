package my.backend.library.service;


import my.backend.library.dto.GenreDto;

import java.util.List;

public interface GenreService {

    List<GenreDto> getGenreById(Long id);
}
