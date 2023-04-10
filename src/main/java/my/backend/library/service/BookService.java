package my.backend.library.service;

import my.backend.library.dto.BookDto;

public interface BookService {

    BookDto getByNameV1(String name);

    BookDto getById1(Long id);

    BookDto getByNameV2(String name);

    BookDto getByNameV3(String name);
}
