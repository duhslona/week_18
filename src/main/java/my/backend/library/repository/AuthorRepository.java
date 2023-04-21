package my.backend.library.repository;

import my.backend.library.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query(value = "from Author a where a.id in " +
            "(select ab.authorBookPK.authorId from AuthorBook ab where ab.authorBookPK.bookId = ?1)")
    Optional<List<Author>> findByBookId(Long bookId);

}
