package my.backend.library.controller;

import lombok.RequiredArgsConstructor;
import my.backend.library.dto.AuthorCreateDto;
import my.backend.library.dto.AuthorDto;
import my.backend.library.dto.AuthorUpdateDto;
import my.backend.library.service.AuthorService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/author/{id}")
    AuthorDto getAuthorById(@PathVariable("id") Long id) {
        return authorService.getAuthorById(id);
    }

    @GetMapping("/author")
    List<AuthorDto> getAuthorByName(@RequestParam("name") String name) {
        return authorService.getAuthorByName(name);
    }

    @GetMapping("/author/v2")
    List<AuthorDto> getAuthorByNameV2(@RequestParam("name") String name) {
        return authorService.getAuthorByName(name);
    }

    @GetMapping("/author/v3")
    List<AuthorDto> getAuthorByNameV3(@RequestParam("name") String name) {
        return authorService.getByNameV3(name);
    }

    @PostMapping("/author/create")
    AuthorDto createAuthor(@RequestBody AuthorCreateDto authorCreateDto) {
        return authorService.createAuthor(authorCreateDto);
    }

    @PutMapping("/author/update")
    AuthorDto updateAuthor(@RequestBody AuthorUpdateDto authorUpdateDto) {
        return authorService.updateAuthor(authorUpdateDto);
    }

    @DeleteMapping("/author/delete/{id}")
    void deleteAuthor(@PathVariable("id") Long id) {
        authorService.deleteAuthor(id);
    }

    @PatchMapping("/author/patch/{id}")
    AuthorDto updatePartAuthor(@PathVariable("id") Long id, @RequestBody AuthorUpdateDto authorUpdateDto) {
        return authorService.updatePartAuthor(id, authorUpdateDto);
    }
}
