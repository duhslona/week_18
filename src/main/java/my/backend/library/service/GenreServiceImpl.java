package my.backend.library.service;


import lombok.RequiredArgsConstructor;
import my.backend.library.dto.BookDto;
import my.backend.library.dto.GenreDto;
import my.backend.library.model.Genre;
import my.backend.library.repository.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public GenreDto getGenreById(Long id) {
//        Genre genre = genreRepository.findById(id).orElseThrow();
        List<Genre> genre = genreRepository.findById1(id);
        return convertToDto(genre.get(0));
    }

    private GenreDto convertToDto(Genre genre) {
        List<BookDto> bookDtoList = genre.getBooks()
                .stream()
                .map(book -> BookDto.builder()
                        .name(book.getName())
                        .authors(book.getAuthors().stream().map(author -> (author.getName() + " " + author.getSurname())).collect(Collectors.toList()))
                        .build()
                ).toList();
        return GenreDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .books(bookDtoList)
                .build();
    }
}
