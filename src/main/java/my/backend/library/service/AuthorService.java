package my.backend.library.service;

import my.backend.library.dto.AuthorDto;

public interface AuthorService {

    AuthorDto getAuthorById(Long id);
}
