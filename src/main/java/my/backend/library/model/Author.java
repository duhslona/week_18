package my.backend.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Column(nullable = false)
    private String surname;

    @ManyToMany(mappedBy = "authors")
    private Set<Book> books;

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Author))
            return false;
        Author other = (Author) o;
        boolean idEquals = (this.getId() == null && other.getId() == null)
                || (this.getId() != null && this.getId().equals(other.getId()));
        boolean nameEquals = (this.getName() == null && other.getName() == null)
                || (this.getName() != null && this.getName().equals(other.getName()));
        boolean surnameEquals = (this.getSurname() == null && other.getSurname() == null)
                || (this.getSurname() != null && this.getSurname().equals(other.getSurname()));
        boolean booksEquals = (this.getBooks() == null && other.getBooks() == null)
                || (this.getBooks().isEmpty() && other.getBooks().isEmpty())
                || (this.getBooks().size() == other.getBooks().size() && this.getBooks().containsAll(other.getBooks()));
        return (idEquals && nameEquals && surnameEquals && booksEquals);
    }


}
