package my.backend.library.repository;

import my.backend.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query(value = "from Book b where b.genre.id = ?1")
    Optional<List<Book>> findByGenreId(Long genreId);
}
