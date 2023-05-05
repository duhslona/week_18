package my.backend.library;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import my.backend.library.dto.AuthorCreateDto;
import my.backend.library.dto.AuthorDto;
import my.backend.library.dto.AuthorUpdateDto;
import my.backend.library.model.Author;
import my.backend.library.model.Book;
import my.backend.library.repository.AuthorRepository;
import my.backend.library.service.AuthorServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private Author author;

    @BeforeEach
    public void setupAuthor() {
        Long id = 1L;
        String name = "John";
        String surname = "Doe";
        Set<Book> books = new HashSet<>();
        author = new Author(id, name, surname, books);
    }

    @Test
    public void testGetAuthorById() {
        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));

        AuthorDto authorDto = authorService.getAuthorById(author.getId());

        verify(authorRepository).findById(author.getId());
        Assertions.assertEquals(authorDto.getId(), author.getId());
        Assertions.assertEquals(authorDto.getName(), author.getName());
        Assertions.assertEquals(authorDto.getSurname(), author.getSurname());
    }

    @Test
    public void testGetAuthorByIdNotFound() {
        Long id = 1L;
        when(authorRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> authorService.getAuthorById(id));

        verify(authorRepository).findById(id);
    }

    @Test
    public void getAuthorByName() {
        when(authorRepository.findAuthorByName(author.getName())).thenReturn(Optional.of(Collections.singletonList(author)));

        List<AuthorDto> authorDto = authorService.getAuthorByName(author.getName());

        verify(authorRepository).findAuthorByName(author.getName());
        Assertions.assertEquals(authorDto.size(), 1);
        Assertions.assertEquals(authorDto.get(0).getId(), author.getId());
        Assertions.assertEquals(authorDto.get(0).getName(), author.getName());
        Assertions.assertEquals(authorDto.get(0).getSurname(), author.getSurname());
    }

    @Test
    public void getAuthorsByNameFewResults() {
        Long id = 2L;
        String name = author.getName();
        String surname = "Twain";
        Set<Book> books = new HashSet<>();
        Author author2 = new Author(id, name, surname, books);

        when(authorRepository.findAuthorByName(author.getName())).thenReturn(Optional.of(Arrays.asList(author, author2)));

        List<AuthorDto> authorDto = authorService.getAuthorByName(author.getName());

        verify(authorRepository).findAuthorByName(author.getName());
        Assertions.assertEquals(authorDto.size(), 2);
        Assertions.assertEquals(authorDto.get(0).getId(), author.getId());
        Assertions.assertEquals(authorDto.get(0).getName(), author.getName());
        Assertions.assertEquals(authorDto.get(0).getSurname(), author.getSurname());
        Assertions.assertEquals(authorDto.get(1).getId(), author2.getId());
        Assertions.assertEquals(authorDto.get(1).getName(), author2.getName());
        Assertions.assertEquals(authorDto.get(1).getSurname(), author2.getSurname());
    }

    @Test
    public void getAuthorByNameNotFound() {
        when(authorRepository.findAuthorByName(author.getName())).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> authorService.getAuthorByName(author.getName()));

        verify(authorRepository).findAuthorByName(author.getName());
    }

    @Test
    public void getAuthorByNameV2() {
        when(authorRepository.findAuthorByNameBySql(author.getName())).thenReturn(Optional.of(Collections.singletonList(author)));

        List<AuthorDto> authorDto = authorService.getAuthorByNameV2(author.getName());

        verify(authorRepository).findAuthorByNameBySql(author.getName());
        Assertions.assertEquals(authorDto.size(), 1);
        Assertions.assertEquals(authorDto.get(0).getId(), author.getId());
        Assertions.assertEquals(authorDto.get(0).getName(), author.getName());
        Assertions.assertEquals(authorDto.get(0).getSurname(), author.getSurname());
    }

    @Test
    public void getAuthorsByNameV2FewResults() {
        Long id = 2L;
        String name = author.getName();
        String surname = "Twain";
        Set<Book> books = new HashSet<>();
        Author author2 = new Author(id, name, surname, books);

        when(authorRepository.findAuthorByNameBySql(author.getName())).thenReturn(Optional.of(Arrays.asList(author, author2)));

        List<AuthorDto> authorDto = authorService.getAuthorByNameV2(author.getName());

        verify(authorRepository).findAuthorByNameBySql(author.getName());
        Assertions.assertEquals(authorDto.size(), 2);
        Assertions.assertEquals(authorDto.get(0).getId(), author.getId());
        Assertions.assertEquals(authorDto.get(0).getName(), author.getName());
        Assertions.assertEquals(authorDto.get(0).getSurname(), author.getSurname());
        Assertions.assertEquals(authorDto.get(1).getId(), author2.getId());
        Assertions.assertEquals(authorDto.get(1).getName(), author2.getName());
        Assertions.assertEquals(authorDto.get(1).getSurname(), author2.getSurname());
    }

    @Test
    public void getAuthorByNameV2NotFound() {
        when(authorRepository.findAuthorByNameBySql(author.getName())).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> authorService.getAuthorByNameV2(author.getName()));

        verify(authorRepository).findAuthorByNameBySql(author.getName());
    }

    @Test()
    public void getAuthorByNameV3() {
        Specification<Author> specification = new Specification<>() {
            @Override
            public Predicate toPredicate(Root<Author> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("name"), author.getName());
            }
        };

        when(authorRepository.findAll()).thenReturn(Collections.singletonList(author));

        List<AuthorDto> authorDto = authorService.getByNameV3(author.getName());

//        verify(authorRepository).findAll(specification);
        Assertions.assertEquals(authorDto.size(), 1);
        Assertions.assertEquals(authorDto.get(0).getId(), author.getId());
        Assertions.assertEquals(authorDto.get(0).getName(), author.getName());
        Assertions.assertEquals(authorDto.get(0).getSurname(), author.getSurname());
    }

    @Test
    public void testCreateAuthor() {
        Long id = 1L;
        String name = "New";
        String surname = "surname";
        Set<Book> books = new HashSet<>();

        Author authorToSave = new Author(null, name, surname, null);
        Author authorResult = new Author(id, name, surname, books);

        when(authorRepository.save(authorToSave)).thenReturn(authorResult);

        AuthorCreateDto authorDtoRequest = new AuthorCreateDto(name, surname);
        AuthorDto authorDtoResponse = authorService.createAuthor(authorDtoRequest);

        verify(authorRepository).save(authorToSave);
        Assertions.assertEquals(authorDtoResponse.getId(), id);
        Assertions.assertEquals(authorDtoResponse.getName(), name);
        Assertions.assertEquals(authorDtoResponse.getSurname(), surname);
    }

    @Test
    public void deleteAuthorTest() {
        authorService.deleteAuthor(author.getId());

        verify(authorRepository).deleteById(author.getId());
    }

    @Test
    public void getAllAuthorsTest() {
        Long id = 2L;
        String name = author.getName();
        String surname = "Twain";
        Set<Book> books = new HashSet<>();
        Author author2 = new Author(id, name, surname, books);

        when(authorRepository.findAll()).thenReturn((Arrays.asList(author, author2)));

        List<AuthorDto> authorsDto = authorService.getAllAuthors();

        verify(authorRepository).findAll();
        Assertions.assertEquals(2, authorsDto.size());
        Assertions.assertEquals(author.getId(), authorsDto.get(0).getId());
        Assertions.assertEquals(author.getName(), authorsDto.get(0).getName());
        Assertions.assertEquals(author.getSurname(), authorsDto.get(0).getSurname());
        Assertions.assertEquals(author2.getId(), authorsDto.get(1).getId());
        Assertions.assertEquals(author2.getName(), authorsDto.get(1).getName());
        Assertions.assertEquals(author2.getSurname(), authorsDto.get(1).getSurname());
    }

    @Test
    public void getAllAuthorsNoElementsTest() {
        when(authorRepository.findAll()).thenReturn(Collections.emptyList());

        List<AuthorDto> authorsDto = authorService.getAllAuthors();

        verify(authorRepository).findAll();
        Assertions.assertEquals(0, authorsDto.size());
    }

    @Test
    public void testUpdateAuthor() {
        Author updatedAuthor = new Author(author.getId(), "New name", "New surname", new HashSet<>());
        AuthorUpdateDto updateAuthorDto = new AuthorUpdateDto(updatedAuthor.getId(), updatedAuthor.getName(), updatedAuthor.getSurname(), new ArrayList<>());

        when(authorRepository.findById(updatedAuthor.getId())).thenReturn(Optional.of(author));
        when(authorRepository.save(updatedAuthor)).thenReturn(updatedAuthor);

        AuthorDto authorDto = authorService.updateAuthor(updateAuthorDto);

        verify(authorRepository).findById(updatedAuthor.getId());
        verify(authorRepository).save(updatedAuthor);

        Assertions.assertEquals(updatedAuthor.getId(), authorDto.getId());
        Assertions.assertEquals(updatedAuthor.getName(), authorDto.getName());
        Assertions.assertEquals(updatedAuthor.getSurname(), authorDto.getSurname());
    }

    @Test
    public void testUpdateAuthorNotFound() {
        when(authorRepository.findById(author.getId())).thenReturn(Optional.empty());
        AuthorUpdateDto updateAuthorDto = new AuthorUpdateDto(author.getId(), author.getName(), author.getSurname(), new ArrayList<>());

        Assertions.assertThrows(NoSuchElementException.class, () -> authorService.updateAuthor(updateAuthorDto));

        verify(authorRepository).findById(author.getId());
    }

    @Test
    public void testUpdatePartAuthor() {
        Author updateRequestAuthor = new Author(author.getId(), "New name", author.getSurname(), author.getBooks());
        AuthorUpdateDto updateAuthorDto = new AuthorUpdateDto(null, "New name", null, null);

        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
        when(authorRepository.save(updateRequestAuthor)).thenReturn(updateRequestAuthor);

        AuthorDto authorDto = authorService.updatePartAuthor(author.getId(), updateAuthorDto);

        verify(authorRepository).findById(author.getId());
        verify(authorRepository).save(updateRequestAuthor);

        Assertions.assertEquals(author.getId(), authorDto.getId());
        Assertions.assertEquals(updateRequestAuthor.getName(), authorDto.getName());
        Assertions.assertEquals(author.getSurname(), authorDto.getSurname());
    }

    @Test
    public void testUpdatePartAuthorNotFound() {
        when(authorRepository.findById(author.getId())).thenReturn(Optional.empty());
        AuthorUpdateDto updateAuthorDto = new AuthorUpdateDto(null, "new name", null, null);

        Assertions.assertThrows(NoSuchElementException.class, () -> authorService.updatePartAuthor(author.getId(), updateAuthorDto));

        verify(authorRepository).findById(author.getId());
    }
}
