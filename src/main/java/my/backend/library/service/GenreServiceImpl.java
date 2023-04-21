package my.backend.library.service;


import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
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
}
