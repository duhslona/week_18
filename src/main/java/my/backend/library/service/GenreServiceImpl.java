package my.backend.library.service;


import lombok.RequiredArgsConstructor;
import my.backend.library.dto.BookDto;
import my.backend.library.dto.GenreDto;
import my.backend.library.dto.GenreWithBooksDto;
import my.backend.library.model.Genre;
import my.backend.library.repository.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public List<GenreDto> getGenreById(Long id) {
        List<String[]> genresWithBooks = genreRepository.findCustom(id).orElseThrow();
        return convertToDto(genresWithBooks);
    }


    private List<GenreDto> convertToDto(List<String[]> genresWithBooks) {
        List<GenreWithBooksDto> genresWithBooksDtoList = genresWithBooks.stream().map(x -> new GenreWithBooksDto(x[0], x[1], x[2], x[3])).collect(Collectors.toList());

        List<GenreDto> result = new ArrayList<>();

        for (GenreWithBooksDto genre : genresWithBooksDtoList) {
            if (listContainsGenre(result, genre.getGenreName())) {

            } else{
                result.add()
            }
//        }
        }
    }

    private GenreDto convertToDto(GenreWithBooksDto genreWithBooksDto){
        return GenreDto.builder()
                .name(genreWithBooksDto.getGenreName())
                .books()
    }
    private GenreDto convertToDto(Genre genre) {
        List<BookDto> bookDtoList = genre.getBooks()
                .stream()
                .map(book -> BookDto.builder()
                        .name(book.getName())
                        .authors(book.getAuthors().stream().map(author -> (author.getName() + " " + author.getSurname())).collect(Collectors.toList()))
                        .build()
                ).toList();
        return GenreDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .books(bookDtoList)
                .build();
    }

    private List<GenreDto> convertToDto(GenreWithBooksDto[] genres) {
//        List<GenreDto> result = new ArrayList<>();
//
//        for (String genre : genres) {
//            if (result.contains(result, genre.))
//        }
        return null;
//        List<BookDto> bookDtoList = genre.getBooks()
//                .stream()
//                .map(book -> BookDto.builder()
//                        .name(book.getName())
//                        .authors(book.getAuthors().stream().map(author -> (author.getName() + " " + author.getSurname())).collect(Collectors.toList()))
//                        .build()
//                ).toList();
//        return GenreDto.builder()
//                .id(genre.getId())
//                .name(genre.getName())
//                .books(bookDtoList)
//                .build();
    }

    private boolean listContainsGenre(List<GenreDto> genreList, String genreName) {
        for (GenreDto genreDto : genreList) {
            if (genreDto.getName().equals(genreName)) {
                return true;
            }
        }
        return false;
    }

    private boolean listContainsBook(){

    }
}
