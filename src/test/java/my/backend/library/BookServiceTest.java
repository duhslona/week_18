package my.backend.library;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import my.backend.library.dto.BookCreateDto;
import my.backend.library.dto.BookDto;
import my.backend.library.dto.BookUpdateDto;
import my.backend.library.model.Author;
import my.backend.library.model.Book;
import my.backend.library.model.Genre;
import my.backend.library.repository.AuthorRepository;
import my.backend.library.repository.BookRepository;
import my.backend.library.repository.GenreRepository;
import my.backend.library.service.BookServiceImpl;
import my.backend.library.utils.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BookServiceTest {

    @Mock
    BookRepository bookRepository;
    @Mock
    GenreRepository genreRepository;
    @Mock
    AuthorRepository authorRepository;

    @InjectMocks
    BookServiceImpl bookService;

    private Book book;

    @BeforeEach
    public void setupBook() {
        book = new Book(1L, "Book name", new Genre(1L, "Genre", new HashSet<>()), new HashSet<>());
    }

    @Test
    public void testGetBookById1() {
        when(bookRepository.findBookById(book.getId())).thenReturn(Optional.of(book));

        BookDto bookDto = bookService.getById1(book.getId());

        verify(bookRepository).findBookById(book.getId());
        Assertions.assertEquals(book.getId(), bookDto.getId());
        Assertions.assertEquals(book.getName(), bookDto.getName());
        Assertions.assertEquals(book.getGenre().getName(), bookDto.getGenre());
    }

    @Test
    public void testGetBookByIdNotFound() {
        Long id = 1L;
        when(bookRepository.findBookById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> bookService.getById1(id));

        verify(bookRepository).findBookById(id);
    }

    @Test
    public void testGetBookByNameV1() {
        when(bookRepository.findBookByName(book.getName())).thenReturn(Optional.of(book));

        BookDto bookDto = bookService.getByNameV1(book.getName());

        verify(bookRepository).findBookByName(book.getName());
        Assertions.assertEquals(book.getId(), bookDto.getId());
        Assertions.assertEquals(book.getName(), bookDto.getName());
        Assertions.assertEquals(book.getGenre().getName(), bookDto.getGenre());
    }

    @Test
    public void testGetBookByNameV1NotFound() {
        String name = RandomUtils.getRandomString(10);
        when(bookRepository.findBookByName(name)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> bookService.getByNameV1(name));

        verify(bookRepository).findBookByName(name);
    }


    @Test
    public void testGetBookByNameV2() {
        when(bookRepository.findBookByNameBySql(book.getName())).thenReturn(Optional.of(book));

        BookDto bookDto = bookService.getByNameV2(book.getName());

        verify(bookRepository).findBookByNameBySql(book.getName());
        Assertions.assertEquals(book.getId(), bookDto.getId());
        Assertions.assertEquals(book.getName(), bookDto.getName());
        Assertions.assertEquals(book.getGenre().getName(), bookDto.getGenre());
    }

    @Test
    public void testGetBookByNameV2NotFound() {
        String name = RandomUtils.getRandomString(10);
        when(bookRepository.findBookByNameBySql(name)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> bookService.getByNameV2(name));

        verify(bookRepository).findBookByNameBySql(name);
    }

    @Test
    public void testGetBookByNameV3() {
        Specification<Book> specification = Specification
                .where(new Specification<>() {
                    @Override
                    public Predicate toPredicate(Root<Book> root,
                                                 CriteriaQuery<?> query,
                                                 CriteriaBuilder criteriaBuilder) {
                        return criteriaBuilder.equal(root.get("name"), book.getName());
                    }
                });

        when(bookRepository.findOne(specification)).thenReturn(Optional.of(book));

        BookDto bookDto = bookService.getByNameV3(book.getName());

        verify(bookRepository).findOne(specification);
        Assertions.assertEquals(book.getId(), bookDto.getId());
        Assertions.assertEquals(book.getName(), bookDto.getName());
        Assertions.assertEquals(book.getGenre().getName(), bookDto.getGenre());
    }

    @Test
    public void testCreateBook() {
        Book bookToSave = new Book(null, book.getName(), book.getGenre(), book.getAuthors());
        BookCreateDto bookCreateDto = new BookCreateDto(book.getName(), Collections.emptyList(), book.getGenre().getId());
        when(bookRepository.save(bookToSave)).thenReturn(book);
        when(genreRepository.findById(book.getGenre().getId())).thenReturn(Optional.of(book.getGenre()));
        when(authorRepository.findAuthorByIdsBySql(Collections.emptyList())).thenReturn(Optional.of(book.getAuthors()));

        BookDto bookDto = bookService.createBook(bookCreateDto);

        verify(bookRepository).save(bookToSave);
        verify(genreRepository).findById(book.getGenre().getId());
        verify(authorRepository).findAuthorByIdsBySql(new ArrayList<>());
        Assertions.assertEquals(book.getId(), bookDto.getId());
        Assertions.assertEquals(book.getName(), bookDto.getName());
        Assertions.assertEquals(book.getGenre().getName(), bookDto.getGenre());
    }

    @Test
    public void testUpdateBook() {
        Book bookToUpdate = new Book(book.getId(), RandomUtils.getRandomString(10), book.getGenre(), book.getAuthors());
        BookUpdateDto bookUpdateDto = new BookUpdateDto(bookToUpdate.getId(), bookToUpdate.getName(),
                Collections.emptyList(), bookToUpdate.getGenre().getId());

        when(bookRepository.save(bookToUpdate)).thenReturn(bookToUpdate);
        when(genreRepository.findById(book.getGenre().getId())).thenReturn(Optional.of(book.getGenre()));
        when(authorRepository.findAuthorByIdsBySql(Collections.emptyList())).thenReturn(Optional.of(book.getAuthors()));

        BookDto bookDto = bookService.updateBook(bookUpdateDto);

        verify(bookRepository).save(bookToUpdate);
        verify(genreRepository).findById(book.getGenre().getId());
        verify(authorRepository).findAuthorByIdsBySql(new ArrayList<>());

        Assertions.assertEquals(bookToUpdate.getId(), bookDto.getId());
        Assertions.assertEquals(bookToUpdate.getName(), bookDto.getName());
        Assertions.assertEquals(bookToUpdate.getGenre().getName(), bookDto.getGenre());
    }

    @Test
    public void testDeleteBook() {
        bookService.deleteBook(book.getId());

        verify(bookRepository).deleteById(book.getId());
    }

    @Test
    public void getAllBooksTest() {
        Book book2 = new Book(2L, "Name2", new Genre(1L, "Genre2", new HashSet<>()), new HashSet<>());
        List<Book> books = new ArrayList<>();
        books.add(book);
        books.add(book2);

        when(bookRepository.findAll()).thenReturn((books));

        List<BookDto> booksDto = bookService.getAllBooks();

        verify(bookRepository).findAll();

        Assertions.assertEquals(book.getId(), booksDto.get(0).getId());
        Assertions.assertEquals(book.getName(), booksDto.get(0).getName());
        Assertions.assertEquals(book.getGenre().getName(), booksDto.get(0).getGenre());

        Assertions.assertEquals(book2.getId(), booksDto.get(1).getId());
        Assertions.assertEquals(book2.getName(), booksDto.get(1).getName());
        Assertions.assertEquals(book2.getGenre().getName(), booksDto.get(1).getGenre());
    }
}
