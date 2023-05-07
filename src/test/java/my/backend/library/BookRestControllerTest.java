package my.backend.library;

import com.fasterxml.jackson.core.type.TypeReference;
import my.backend.library.dto.BookCreateDto;
import my.backend.library.dto.BookDto;
import my.backend.library.dto.BookUpdateDto;
import my.backend.library.utils.JsonUtils;
import my.backend.library.utils.RandomUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class BookRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private BookDto bookDto;

    @BeforeEach
    public void addBook() throws Exception {
        BookCreateDto bookCreateDto = new BookCreateDto();
        bookCreateDto.setName(RandomUtils.getRandomString(10));
        bookCreateDto.setGenreId(2L);
        bookCreateDto.setAuthorIds(Collections.singletonList(1L));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/book/create")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(JsonUtils.getJson(bookCreateDto)))
                .andExpect(status().isOk())
                .andReturn();

        bookDto = JsonUtils.fromJson(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
    }

    @AfterEach
    public void removeBook() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/book/delete/" + bookDto.getId().toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetBookByName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/book?name={name}", bookDto.getName()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(bookDto.getName()));
    }

    @Test
    public void testGetBookById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/book/{id}", bookDto.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(bookDto.getName()));
    }

    @Test
    public void testGetBookByNameV2() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/book/v2?name={name}", bookDto.getName()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(bookDto.getName()));
    }

    @Test
    public void testGetBookByNameV3() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/book/v3?name={name}", bookDto.getName()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(bookDto.getName()));
    }

    @Test
    public void testUpdateBook() throws Exception {
        BookUpdateDto bookUpdateDto = new BookUpdateDto();
        bookUpdateDto.setId(bookDto.getId());
        bookUpdateDto.setName(RandomUtils.getRandomString(10));
        bookUpdateDto.setGenreId(1L);
        bookUpdateDto.setAuthorIds(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.put("/book/update")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(JsonUtils.getJson(bookUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookUpdateDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(bookUpdateDto.getName()));
    }

}
