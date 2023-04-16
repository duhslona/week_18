package my.backend.library.repository;

import my.backend.library.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AuthorRepository extends JpaRepository<Author, Long>, JpaSpecificationExecutor<Author> {
    Optional<List<Author>> findAuthorByName(String name);

    @Query(nativeQuery = true, value = "SELECT * FROM AUTHOR WHERE name = ?")
    Optional<List<Author>> findAuthorByNameBySql(String name);

    @Query(nativeQuery = true, value = "SELECT * FROM AUTHOR WHERE id in ?")
    Optional<Set<Author>> findAuthorByIdsBySql(List<Long> ids);

}
