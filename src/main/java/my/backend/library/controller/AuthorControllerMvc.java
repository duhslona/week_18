package my.backend.library.controller;

import lombok.RequiredArgsConstructor;
import my.backend.library.service.AuthorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AuthorControllerMvc {

    private final AuthorService authorService;

    @GetMapping("/authorsMvc")
    String getAuthorsView(Model model) {
        model.addAttribute("authors", authorService.getAllAuthors());
        return "authors";
    }

}
