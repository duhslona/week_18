package my.backend.library.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookDto {
    private Long id;
    private String name;
    private String genre;

    @Transient
    private List<String> authors;
}
