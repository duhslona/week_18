package my.backend.library.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import my.backend.library.dto.AuthorDto;
import my.backend.library.dto.BookDto;
import my.backend.library.model.Author;
import my.backend.library.repository.AuthorRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public AuthorDto getAuthorById(Long id) {
        Author author = authorRepository.findById(id).orElseThrow();
        return convertToDto(author);
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
                .where(new Specification<Author>() {
                    @Override
                    public Predicate toPredicate(Root<Author> root,
                                                 CriteriaQuery<?> query,
                                                 CriteriaBuilder criteriaBuilder) {
                        return criteriaBuilder.equal(root.get("name"), name);
                    }
                });

        List<Author> authors = authorRepository.findAll(specification).stream().collect(Collectors.toList());
        return convertToDto(authors);
    }

    private List<AuthorDto> convertToDto(List<Author> authors) {
        return authors.stream().map(author -> convertToDto(author)).collect(Collectors.toList());
    }

    private AuthorDto convertToDto(Author author) {
        List<BookDto> bookDtoList = author.getBooks()
                .stream()
                .map(book -> BookDto.builder()
                        .name(book.getName())
                        .build()
                ).toList();
        return AuthorDto.builder()
                .books(bookDtoList)
                .id(author.getId())
                .name(author.getName())
                .surname(author.getSurname())
                .build();
    }
}
