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
import org.springframework.util.CollectionUtils;

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
        log.info("Try to find author by id {}.", id);
        Optional<Author> author = authorRepository.findById(id);
        if (author.isPresent()) {
            AuthorDto authorDto = convertToDto(author.get());
            log.info("Author: {}.", authorDto.toString());
            return authorDto;
        } else {
            log.error("Author with id {} not found.", id);
            throw new NoSuchElementException("No value present.");
        }
    }

    @Override
    public List<AuthorDto> getAuthorByName(String name) {
        log.info("Try to find author by name {}.", name);
        Optional<List<Author>> authors = authorRepository.findAuthorByName(name);
        if (authors.isPresent()) {
            List<AuthorDto> authorsDto = convertToDto(authors.get());
            log.info("Authors: {}.", String.join("; ", authorsDto.toString()));
            return authorsDto;
        } else {
            log.error("No authors with name {} were found.", name);
            throw new NoSuchElementException("No value present.");
        }
    }

    @Override
    public List<AuthorDto> getAuthorByNameV2(String name) {
        log.info("Try to find author by name {} V2 api.", name);
        Optional<List<Author>> authors = authorRepository.findAuthorByNameBySql(name);
        if (authors.isPresent()) {
            List<AuthorDto> authorsDto = convertToDto(authors.get());
            log.info("Authors: {}.", String.join("; ", authorsDto.toString()));
            return authorsDto;
        } else {
            log.error("No authors with name {} were found.", name);
            throw new NoSuchElementException("No value present.");
        }
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
        log.info("Try to find author by name {} V2 api.", name);
        List<Author> authors = authorRepository.findAll(specification);
        if (!CollectionUtils.isEmpty(authors)) {
            List<AuthorDto> authorsDto = convertToDto(authors);
            log.info("Authors: {}.", String.join("; ", authorsDto.toString()));
            return authorsDto;
        } else {
            log.error("No authors with name {} were found.", name);
            throw new NoSuchElementException("No value present.");
        }
    }

    @Override
    public AuthorDto createAuthor(AuthorCreateDto authorCreateDto) {
        log.info("Try to save author: {}.", authorCreateDto.toString());
        Author author = authorRepository.save(convertDtoToEntity(authorCreateDto));
        AuthorDto authorDto = convertToDto(author);
        log.info("Author created successfully");
        return authorDto;
    }

    @Override
    public AuthorDto updateAuthor(AuthorUpdateDto authorUpdateDto) {
        log.info("Start to update author with id {} with values {},", authorUpdateDto.getId(),
                authorUpdateDto);
        log.info("Try to find author by id {}.", authorUpdateDto.getId());
        Optional<Author> authorToUpdate = authorRepository.findById(authorUpdateDto.getId());
        if (authorToUpdate.isPresent()) {
            Author author = authorToUpdate.get();
            author.setName(authorUpdateDto.getName());
            author.setSurname(authorUpdateDto.getSurname());
            log.info("Author is found. Update author with new values");
            Author savedAuthor = authorRepository.save(author);
            AuthorDto authorDto = convertToDto(savedAuthor);
            return authorDto;
        } else {
            log.error("Author with id {} not found.", authorUpdateDto.getId());
            throw new NoSuchElementException("No value present.");
        }
    }

    @Override
    public void deleteAuthor(Long id) {
        log.info("Try to remove author by id {}.", id);
        authorRepository.deleteById(id);
    }

    @Override
    public AuthorDto updatePartAuthor(Long id, AuthorUpdateDto authorUpdateDto) {
        log.info("Try to update fields of author with id {} with values {}", id, authorUpdateDto);
        Author author = authorRepository.findById(id).orElseThrow();
        if (authorUpdateDto.getName() != null) {
            author.setName(authorUpdateDto.getName());
        }
        if (authorUpdateDto.getSurname() != null) {
            author.setSurname(authorUpdateDto.getSurname());
        }
        Author savedAuthor = authorRepository.save(author);
        AuthorDto authorDto = convertToDto(savedAuthor);
        log.info("Author with id {} updated successful.", id);
        return authorDto;
    }

    @Override
    public List<AuthorDto> getAllAuthors() {
        log.info("Try to get all authors");
        List<Author> authors = authorRepository.findAll();
        log.info("Authors found: {}.", String.join("; ", authors.toString()));
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
