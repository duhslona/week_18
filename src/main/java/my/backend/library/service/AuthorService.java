package my.backend.library.service;

import my.backend.library.dto.AuthorCreateDto;
import my.backend.library.dto.AuthorDto;
import my.backend.library.dto.AuthorUpdateDto;

import java.util.List;

public interface AuthorService {

    AuthorDto getAuthorById(Long id);

    List<AuthorDto> getAuthorByName(String name);

    List<AuthorDto> getAuthorByNameV2(String name);

    List<AuthorDto> getByNameV3(String name);

    AuthorDto createAuthor(AuthorCreateDto authorCreateDto);

    AuthorDto updateAuthor(AuthorUpdateDto authorUpdateDto);

    void deleteAuthor(Long id);

    AuthorDto updatePartAuthor(Long id, AuthorUpdateDto updateDto);

    List<AuthorDto> getAllAuthors();
}
