package my.backend.library.controller;

import lombok.RequiredArgsConstructor;
import my.backend.library.dto.AuthorDto;
import my.backend.library.service.AuthorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    List<AuthorDto> getAuthorById(@RequestParam("name") String name) {
        return authorService.getAuthorByName(name);
    }

    @GetMapping("/author/v2")
    List<AuthorDto> getAuthorByIdV2(@RequestParam("name") String name) {
        return authorService.getAuthorByName(name);
    }

    @GetMapping("/author/v3")
    List<AuthorDto> getAuthorByIdV3(@RequestParam("name") String name) {
        return authorService.getByNameV3(name);
    }
}
