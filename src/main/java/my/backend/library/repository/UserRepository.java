package my.backend.library.repository;

import my.backend.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //    @Query(nativeQuery = true, value ="SELECT u FROM User u JOIN FETCH u.roles roles")
//    @Query(nativeQuery = true, value ="SELECT u.id, u.login, u.password FROM User u JOIN FETCH u.roles roles")
    @Query(value = "FROM User u join fetch u.roles roles where u.login=?1")
    Optional<User> findByLogin(String login);
}
