package my.backend.library.service;


import lombok.RequiredArgsConstructor;
import my.backend.library.dto.BookDto;
import my.backend.library.dto.BookWithAuthorsDto;
import my.backend.library.dto.GenreDto;
import my.backend.library.dto.GenreWithBooksDto;
import my.backend.library.model.Author;
import my.backend.library.model.Book;
import lombok.extern.slf4j.Slf4j;
import my.backend.library.dto.BookDto;
import my.backend.library.dto.GenreDto;
import my.backend.library.model.Genre;
import my.backend.library.repository.AuthorRepository;
import my.backend.library.repository.BookRepository;
import my.backend.library.repository.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Override
    public GenreWithBooksDto getGenreById(Long id) {
        Genre genre = genreRepository.findById(id).orElseThrow();
        List<Book> books = bookRepository.findByGenreId(id).orElseThrow();

        List<BookWithAuthorsDto> booksWithAuthors = new ArrayList<>();

        for (Book book : books) {
            List<Author> authors = authorRepository.findByBookId(book.getId()).orElseThrow();
            List<String> bookAuthors = new ArrayList<>();
            for (Author author : authors) {
                bookAuthors.add(author.getName() + " " + author.getSurname());
            }
            booksWithAuthors.add(BookWithAuthorsDto.builder()
                    .name(book.getName())
                    .authors(bookAuthors)
                    .build());
        }

        return GenreWithBooksDto.builder()
                .genreName(genre.getName())
                .books(booksWithAuthors)
                .build();
    }

    @Override
    public GenreDto getGenreById(Long id) {
        log.info("Try to find genre by id {}.", id);
        Optional<Genre> genre = genreRepository.findById(id);
        if (genre.isPresent()) {
            GenreDto genreDto = convertToDto(genre.get());
            log.info("Genre: {}.", genreDto);
            return genreDto;
        } else {
            log.error("Can't find genre by id {}.", id);
            throw new NoSuchElementException("No value present.");
        }
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
