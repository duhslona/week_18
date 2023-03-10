package my.backend.library.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import my.backend.library.model.Author;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookDto {
    private Long id;
    private String name;
    private String genre;

    @Transient
    private List<String> authors;
}
