package my.backend.library.repository;

import my.backend.library.model.Role;
import my.backend.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query(value = "from Role r where r.id=(select ur.userRolePK.roleId from UserRole ur " +
            "where ur.userRolePK.userId=(select u.id from User u where u.login=?1))")
    Optional<List<Role>> findByUserName(String login);
}
