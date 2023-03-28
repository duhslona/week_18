package my.backend.library.service;

import my.backend.library.dto.AuthorDto;

import java.util.List;

public interface AuthorService {

    AuthorDto getAuthorById(Long id);

    List<AuthorDto> getAuthorByName(String name);

    List<AuthorDto> getAuthorByNameV2(String name);

    List<AuthorDto> getByNameV3(String name);
}
