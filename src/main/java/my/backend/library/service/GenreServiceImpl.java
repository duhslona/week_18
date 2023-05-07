package my.backend.library.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.backend.library.dto.BookDto;
import my.backend.library.dto.GenreDto;
import my.backend.library.dto.BookWithAuthorsDto;
import my.backend.library.dto.GenreWithBooksDto;
import my.backend.library.model.Author;
import my.backend.library.model.Book;
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
        log.info("Try to find genre by id {}.", id);
        Genre genre = genreRepository.findById(id).orElseThrow();
        log.info("Try to find books by genreId {}.", id);
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

        GenreWithBooksDto genreDto = GenreWithBooksDto.builder()
                .genreName(genre.getName())
                .books(booksWithAuthors)
                .build();
        log.info("Genre: {}.", genreDto);

        return genreDto;
    }
}
