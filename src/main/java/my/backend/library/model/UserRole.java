package my.backend.library.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import java.io.Serializable;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_role")
public class UserRole {

    @EmbeddedId
    private UserRolePK userRolePK;

    @Embeddable
    public class UserRolePK implements Serializable {

        @Column(nullable = false, name = "user_id")
        private Long userId;

        @Column(nullable = false, name = "role_id")
        private Long roleId;

    }

}
