package my.backend.library.service;

import my.backend.library.dto.BookCreateDto;
import my.backend.library.dto.BookDto;
import my.backend.library.dto.BookUpdateDto;

public interface BookService {

    BookDto getByNameV1(String name);

    BookDto getById1(Long id);

    BookDto getByNameV2(String name);

    BookDto getByNameV3(String name);

    BookDto createBook(BookCreateDto bookCreateDto);

    BookDto updateBook(BookUpdateDto bookUpdateDto);

    void deleteBook(Long id);
}
