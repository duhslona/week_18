package my.backend.library;

import com.fasterxml.jackson.core.type.TypeReference;
import my.backend.library.dto.AuthorCreateDto;
import my.backend.library.dto.AuthorDto;
import my.backend.library.dto.AuthorUpdateDto;
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

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AuthorRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private AuthorDto authorDto;

    @BeforeEach
    public void createAuthor() throws Exception {
        AuthorCreateDto authorCreateDto = new AuthorCreateDto();
        authorCreateDto.setName("Name" + RandomUtils.getRandomString(5));
        authorCreateDto.setSurname("Surname" + RandomUtils.getRandomString(5));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/author/create")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(JsonUtils.getJson(authorCreateDto)))
                .andReturn();

        authorDto = JsonUtils.fromJson(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
    }

    @AfterEach
    public void removeAuthor() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/author/delete/" + authorDto.getId()));
    }

    @Test
    public void testGetAuthorById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/author/{id}", authorDto.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorDto.getSurname()));
    }

    @Test
    public void testGetAuthorByName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/author?name=" + authorDto.getName()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname").value(authorDto.getSurname()));
    }

    @Test
    public void testGetAuthorByNameV2() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/author/v2?name=" + authorDto.getName()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname").value(authorDto.getSurname()));
    }

    @Test
    public void testGetAuthorByNameV3() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/author/v3?name=" + authorDto.getName()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname").value(authorDto.getSurname()));
    }

    @Test
    public void testCreateAuthor() throws Exception {
        AuthorCreateDto authorCreateDto = new AuthorCreateDto();
        authorCreateDto.setName("New" + RandomUtils.getRandomString(5));
        authorCreateDto.setSurname("Surname" + RandomUtils.getRandomString(5));

        mockMvc.perform(MockMvcRequestBuilders.post("/author/create")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(JsonUtils.getJson(authorCreateDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorCreateDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorCreateDto.getSurname()));
    }

    @Test
    public void testCreateAuthorIncorrectName() throws Exception {
        AuthorCreateDto authorCreateDto = new AuthorCreateDto();
        authorCreateDto.setName("А");
        authorCreateDto.setSurname("Новый");

        mockMvc.perform(MockMvcRequestBuilders.post("/author/create")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(JsonUtils.getJson(authorCreateDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateAuthorBlankSurname() throws Exception {
        AuthorCreateDto authorCreateDto = new AuthorCreateDto();
        authorCreateDto.setName("Новый");
        authorCreateDto.setSurname("");

        mockMvc.perform(MockMvcRequestBuilders.post("/author/create")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(JsonUtils.getJson(authorCreateDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateAuthor() throws Exception {
        AuthorUpdateDto authorUpdateDto = new AuthorUpdateDto();
        authorUpdateDto.setId(authorDto.getId());
        authorUpdateDto.setName(RandomUtils.getRandomString(10));
        authorUpdateDto.setSurname(RandomUtils.getRandomString(10));

        mockMvc.perform(MockMvcRequestBuilders.put("/author/update")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(JsonUtils.getJson(authorUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorUpdateDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorUpdateDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorUpdateDto.getSurname()));
    }

    @Test
    public void testDeleteAuthor() throws Exception {
        AuthorUpdateDto authorUpdateDto = new AuthorUpdateDto();
        authorUpdateDto.setId(authorDto.getId());
        authorUpdateDto.setName(RandomUtils.getRandomString(10));
        authorUpdateDto.setSurname(RandomUtils.getRandomString(10));

        mockMvc.perform(MockMvcRequestBuilders.delete("/author/delete/" + authorDto.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdatePartAuthor() throws Exception {
        AuthorUpdateDto authorUpdateDto = new AuthorUpdateDto();
        authorUpdateDto.setId(null);
        authorUpdateDto.setName(RandomUtils.getRandomString(10));
        authorUpdateDto.setSurname(null);

        mockMvc.perform(MockMvcRequestBuilders.patch("/author/patch/" + authorDto.getId())
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(JsonUtils.getJson(authorUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorUpdateDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorDto.getSurname()));
    }

}
