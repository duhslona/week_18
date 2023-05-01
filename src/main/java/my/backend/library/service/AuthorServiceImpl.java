package my.backend.library.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.backend.library.dto.AuthorCreateDto;
import my.backend.library.dto.AuthorDto;
import my.backend.library.dto.AuthorUpdateDto;
import my.backend.library.dto.BookDto;
import my.backend.library.model.Author;
import my.backend.library.repository.AuthorRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public AuthorDto getAuthorById(Long id) {
        log.info("Try to find author by id {}", id);
        Optional<Author> author = authorRepository.findById(id);
        if (author.isPresent()) {
            AuthorDto authorDto = convertToDto(author.get());
            log.info("Author: {}", authorDto.toString());
            return authorDto;
        } else {
            log.error("Author with id {} not found", id);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public List<AuthorDto> getAuthorByName(String name) {
        List<Author> authors = authorRepository.findAuthorByName(name).orElseThrow();
        return convertToDto(authors);
    }

    @Override
    public List<AuthorDto> getAuthorByNameV2(String name) {
        List<Author> authors = authorRepository.findAuthorByNameBySql(name).orElseThrow();
        return convertToDto(authors);
    }

    @Override
    public List<AuthorDto> getByNameV3(String name) {
        Specification<Author> specification = Specification
                .where(new Specification<>() {
                    @Override
                    public Predicate toPredicate(Root<Author> root,
                                                 CriteriaQuery<?> query,
                                                 CriteriaBuilder criteriaBuilder) {
                        return criteriaBuilder.equal(root.get("name"), name);
                    }
                });

        List<Author> authors = authorRepository.findAll(specification);
        return convertToDto(authors);
    }

    @Override
    public AuthorDto createAuthor(AuthorCreateDto authorCreateDto) {
        Author author = authorRepository.save(convertDtoToEntity(authorCreateDto));
        AuthorDto authorDto = convertToDto(author);
        return authorDto;
    }

    @Override
    public AuthorDto updateAuthor(AuthorUpdateDto authorUpdateDto) {
        Author author = authorRepository.findById(authorUpdateDto.getId()).orElseThrow();
        author.setName(authorUpdateDto.getName());
        author.setSurname(authorUpdateDto.getSurname());
        Author savedAuthor = authorRepository.save(author);
        AuthorDto authorDto = convertToDto(savedAuthor);
        return authorDto;
    }

    @Override
    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }

    @Override
    public AuthorDto updatePartAuthor(Long id, AuthorUpdateDto authorUpdateDto) {
        Author author = authorRepository.findById(id).orElseThrow();
        if (authorUpdateDto.getName() != null) {
            author.setName(authorUpdateDto.getName());
        }
        if (authorUpdateDto.getSurname() != null) {
            author.setSurname(authorUpdateDto.getSurname());
        }
        Author savedAuthor = authorRepository.save(author);
        AuthorDto authorDto = convertToDto(savedAuthor);
        return authorDto;
    }

    @Override
    public List<AuthorDto> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        return convertToDto(authors);
    }


    private Author convertDtoToEntity(AuthorCreateDto authorCreateDto) {
        return Author.builder()
                .name(authorCreateDto.getName())
                .surname(authorCreateDto.getSurname())
                .build();
    }

    private List<AuthorDto> convertToDto(List<Author> authors) {
        return authors.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private AuthorDto convertToDto(Author author) {
        List<BookDto> bookDtoList = null;
        if (author.getBooks() != null) {
            bookDtoList = author.getBooks()
                    .stream()
                    .map(book -> BookDto.builder()
                            .name(book.getName())
                            .build()
                    ).toList();
        }
        return AuthorDto.builder()
                .books(bookDtoList)
                .id(author.getId())
                .name(author.getName())
                .surname(author.getSurname())
                .build();
    }
}
