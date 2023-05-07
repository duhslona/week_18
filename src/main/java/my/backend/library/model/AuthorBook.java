package my.backend.library.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class AuthorBook {

    @EmbeddedId
    private AuthorBookPK authorBookPK;

    @Embeddable
    public class AuthorBookPK implements Serializable {

        @Column(nullable = false, name = "book_id")
        private Long bookId;

        @Column(nullable = false, name = "author_id")
        private Long authorId;

    }
}
