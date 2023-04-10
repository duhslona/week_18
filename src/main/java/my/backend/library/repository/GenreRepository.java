package my.backend.library.repository;

import my.backend.library.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    @Query(nativeQuery = true, value = "select g.name, b.name, a.name, a.surname from genre g" +
            "    inner join book b on g.id = b.genre_id" +
            "    inner join author_book ab on b.id = ab.book_id" +
            "    inner join author a on a.id = ab.author_id" +
            "where g.id = ?1")
    Optional<Genre> findById(Long id);
}
