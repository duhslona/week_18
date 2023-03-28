package my.backend.library.repository;

import my.backend.library.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    @Query("select b.name, a.name, a.surname from Genre g" +
            "    inner join Book b on g.id = b.genre.id" +
            "    inner join author_book ab on b.id = ab.book_id\n" +
            "    inner join author a on a.id = ab.author_id\n" +
            "where g.id = ?1")
    List<Genre> findById(Long id);
}
