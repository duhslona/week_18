package my.backend.library.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.backend.library.dto.BookCreateDto;
import my.backend.library.dto.BookDto;
import my.backend.library.dto.BookUpdateDto;
import my.backend.library.model.Author;
import my.backend.library.model.Book;
import my.backend.library.model.Genre;
import my.backend.library.repository.AuthorRepository;
import my.backend.library.repository.BookRepository;
import my.backend.library.repository.GenreRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;

    @Override
    public BookDto getByNameV1(String name) {
        log.info("Try to find book by name {}.", name);
        Optional<Book> book = bookRepository.findBookByName(name);
        if (book.isPresent()) {
            BookDto bookDto = convertEntityToDto(book.get());
            log.info("Book: {}.", bookDto.toString());
            return bookDto;
        } else {
            log.error("Can't find book by name {}.", name);
            throw new NoSuchElementException("No value present.");
        }
    }

    @Override
    public BookDto getById1(Long id) {
        log.info("Try to find book by id {}.", id);
        Optional<Book> book = bookRepository.findBookById(id);
        if (book.isPresent()) {
            BookDto bookDto = convertEntityToDto(book.get());
            log.info("Book: {}.", bookDto);
            return bookDto;
        } else {
            log.error("Can't find book by id {}.", id);
            throw new NoSuchElementException("No value present.");
        }
    }

    @Override
    public BookDto getByNameV2(String name) {
        log.info("Try to find book by name {}.", name);
        Optional<Book> book = bookRepository.findBookByNameBySql(name);
        if (book.isPresent()) {
            BookDto bookDto = convertEntityToDto(book.get());
            log.info("Book: {}.", bookDto);
            return bookDto;
        } else {
            log.error("Can't find book by name {}.", name);
            throw new NoSuchElementException("No value present.");
        }
    }

    @Override
    public BookDto getByNameV3(String name) {
        Specification<Book> specification = Specification
                .where(new Specification<>() {
                    @Override
                    public Predicate toPredicate(Root<Book> root,
                                                 CriteriaQuery<?> query,
                                                 CriteriaBuilder criteriaBuilder) {
                        return criteriaBuilder.equal(root.get("name"), name);
                    }
                });
        Optional<Book> book = bookRepository.findOne(specification);
        if (book.isPresent()) {
            BookDto bookDto = convertEntityToDto(book.get());
            log.info("Book: {}.", bookDto);
            return bookDto;
        } else {
            log.error("Can't find book by name {}.", name);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public BookDto createBook(BookCreateDto bookCreateDto) {
        log.info("Try to create new book {}.", bookCreateDto);
        Book book = bookRepository.save(convertDtoToEntity(bookCreateDto));
        BookDto bookDto = convertEntityToDto(book);
        log.info("Book created successfully");
        return bookDto;
    }

    @Override
    public BookDto updateBook(BookUpdateDto bookUpdateDto) {
        log.info("Try to update book with id {} with values {}.", bookUpdateDto.getId(), bookUpdateDto);
        Book book = convertDtoToEntity(bookUpdateDto);
        Book savedBook = bookRepository.save(book);
        log.info("===> " + savedBook.getName());
        return convertEntityToDto(savedBook);
    }

    @Override
    public void deleteBook(Long id) {
        log.info("delete book with id {}.", id);
        bookRepository.deleteById(id);
        log.info("Book removed.");
    }

    @Override
    public List<BookDto> getAllBooks() {
        log.info("Try to get all existing books.");
        List<Book> books = bookRepository.findAll();
        return books.stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    private BookDto convertEntityToDto(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .genre(book.getGenre().getName())
                .name(book.getName())
                .build();
    }

    private Book convertDtoToEntity(BookCreateDto bookCreateDto) {
        Genre genre = genreRepository.findById(bookCreateDto.getGenreId()).orElseThrow();
        Set<Author> authors = authorRepository.findAuthorByIdsBySql(bookCreateDto.getAuthorIds()).orElseThrow();
        return Book.builder()
                .name(bookCreateDto.getName())
                .authors(authors)
                .genre(genre)
                .build();
    }

    private Book convertDtoToEntity(BookUpdateDto bookUpdateDto) {
        Genre genre = genreRepository.findById(bookUpdateDto.getGenreId()).orElseThrow();
        Set<Author> authors = authorRepository.findAuthorByIdsBySql(bookUpdateDto.getAuthorIds()).orElseThrow();

        return Book.builder()
                .id(bookUpdateDto.getId())
                .name(bookUpdateDto.getName())
                .authors(authors)
                .genre(genre)
                .build();
    }
}
