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
public class GenreDto {

    private Long id;
    private String name;

    @Transient
    private List<BookDto> books;

}
