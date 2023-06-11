package my.backend.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookCreateDto {
    @NotBlank(message = "Название книги не должно быть пустым")
    @Size(min = 2, message = "Название книги должно быть не короче 2 символов")
    private String name;

    private List<Long> authorIds;
    @NotNull(message = "Книга должна иметь жанр")
    private Long genreId;
}
