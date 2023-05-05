package my.backend.library;

import com.fasterxml.jackson.core.type.TypeReference;
import my.backend.library.dto.AuthorCreateDto;
import my.backend.library.dto.AuthorDto;
import my.backend.library.dto.AuthorUpdateDto;
import my.backend.library.utils.JsonUtils;
import my.backend.library.utils.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AuthorRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAuthorById() throws Exception {
        Long authorId = 1L;
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(authorId);
        authorDto.setName("Александр");
        authorDto.setSurname("Пушкин");

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/author/{id}", authorId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorDto.getSurname()));
    }

    @Test
    public void testGetAuthorByName() throws Exception {
        Long authorId = 1L;
        String name = "Александр";
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(authorId);
        authorDto.setName(name);
        authorDto.setSurname("Пушкин");

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/author?name=" + name))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname").value(authorDto.getSurname()));
    }

    @Test
    public void testGetAuthorByNameV2() throws Exception {
        Long authorId = 1L;
        String name = "Александр";
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(authorId);
        authorDto.setName(name);
        authorDto.setSurname("Пушкин");

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/author/v2?name=" + name))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname").value(authorDto.getSurname()));
    }

    @Test
    public void testGetAuthorByNameV3() throws Exception {
        Long authorId = 1L;
        String name = "Александр";
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(authorId);
        authorDto.setName(name);
        authorDto.setSurname("Пушкин");

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/author/v3?name=" + name))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname").value(authorDto.getSurname()));
    }

    @Test
    public void testCreateAuthor() throws Exception {
        AuthorCreateDto authorCreateDto = new AuthorCreateDto();
        authorCreateDto.setName("Новый");
        authorCreateDto.setSurname("Автор");

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
        //create author for test
        AuthorCreateDto authorCreateDto = new AuthorCreateDto();
        authorCreateDto.setName("Тест" + RandomUtils.getRandomString(5));
        authorCreateDto.setSurname("Автор" + RandomUtils.getRandomString(5));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/author/create")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(JsonUtils.getJson(authorCreateDto)))
                .andReturn();

        AuthorDto authorDto = JsonUtils.fromJson(result.getResponse().getContentAsString(), new TypeReference<>() {
        });

        //Update author
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
        //create author for test
        AuthorCreateDto authorCreateDto = new AuthorCreateDto();
        authorCreateDto.setName("Тест" + RandomUtils.getRandomString(5));
        authorCreateDto.setSurname("Автор" + RandomUtils.getRandomString(5));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/author/create")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(JsonUtils.getJson(authorCreateDto)))
                .andReturn();

        AuthorDto authorDto = JsonUtils.fromJson(result.getResponse().getContentAsString(), new TypeReference<>() {
        });

        //Delete author
        AuthorUpdateDto authorUpdateDto = new AuthorUpdateDto();
        authorUpdateDto.setId(authorDto.getId());
        authorUpdateDto.setName(RandomUtils.getRandomString(10));
        authorUpdateDto.setSurname(RandomUtils.getRandomString(10));

        mockMvc.perform(MockMvcRequestBuilders.delete("/author/delete/" + authorDto.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdatePartAuthor() throws Exception {
        //create author for test
        AuthorCreateDto authorCreateDto = new AuthorCreateDto();
        authorCreateDto.setName("Test" + RandomUtils.getRandomString(5));
        authorCreateDto.setSurname("Author" + RandomUtils.getRandomString(5));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/author/create")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(JsonUtils.getJson(authorCreateDto)))
                .andReturn();

        AuthorDto authorDto = JsonUtils.fromJson(result.getResponse().getContentAsString(), new TypeReference<>() {
        });

        //Update author
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
