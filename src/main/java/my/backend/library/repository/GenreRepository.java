package my.backend.library.repository;

import my.backend.library.dto.GenreWithBooksDto;
import my.backend.library.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

//    @Query(value = "select g.name, b.name, a.name, a.surname from Genre g " +
//            "inner join Book b on g.id = b.genre.id " +
//            "inner join AuthorBook ab on b.id = ab.authorBookPK.bookId " +
//            "inner join Author a on a.id = ab.authorBookPK.authorId " +
//            "where g.id = ?1")
//    Optional<List<GenreWithBooksDto>> findCustom(Long id);
}

