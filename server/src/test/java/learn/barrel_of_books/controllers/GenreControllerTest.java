package learn.barrel_of_books.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import learn.barrel_of_books.data.GenreRepository;
import learn.barrel_of_books.models.Genre;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GenreControllerTest {
    @MockBean
    GenreRepository repository;

    @Autowired
    MockMvc mvc;

    // READ
    @Test
    void shouldFindAll() throws Exception {
        List<Genre> expected = new ArrayList<>();
        expected.add(makeExistingGenre());
        expected.add(new Genre(2, "Adventure", "Adventure description"));
        expected.add(new Genre(3, "Romance", "Romance description"));
        expected.add(new Genre(4, "Horror", "Horror description"));

        Mockito.when(repository.findAll()).thenReturn(expected);

        String expectedJson = generateJson(expected);

        mvc.perform(get("/api/genre"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void shouldFindById() throws Exception {
        Genre genre = makeExistingGenre();

        Mockito.when(repository.findById(1)).thenReturn(genre);

        String expectedJson = generateJson(genre);

        mvc.perform(get("/api/genre/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    // CREATE
    @Test
    void shouldAdd() throws Exception {
        Genre genre = makeNewGenre();
        Genre expected = makeNewGenre();
        expected.setGenreId(5);

        Mockito.when(repository.add(genre)).thenReturn(expected);

        String inputJson = generateJson(genre);
        String expectedJson = generateJson(expected);

        var request = post("/api/genre")
                .header("Authorization", TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void shouldNotAddEmptyName() throws Exception{
        Genre genre = makeNewGenre();
        genre.setName(" ");

        String inputJson = generateJson(genre);

        var request = post("/api/genre")
                .header("Authorization", TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotAddDuplicateName() throws Exception{
        Genre genre = makeNewGenre();
        Genre expected = makeNewGenre();
        expected.setGenreId(5);

        Mockito.when(repository.findByName(genre.getName())).thenReturn(expected);

        String inputJson = generateJson(genre);

        var request = post("/api/genre")
                .header("Authorization", TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    //UPDATE
    @Test
    void shouldUpdate() throws Exception {
        Genre genre = makeExistingGenre();

        Mockito.when(repository.update(genre)).thenReturn(true);

        String genreJson = generateJson(genre);

        var request = put("/api/genre/1")
                .header("Authorization", TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(genreJson);

        mvc.perform(request).andExpect(status().isNoContent());
    }

    @Test
    void shouldNotUpdateConflict() throws Exception {
        Genre genre = makeExistingGenre();

        String genreJson = generateJson(genre);

        var request = put("/api/genre/10")
                .header("Authorization", TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(genreJson);

        mvc.perform(request).andExpect(status().isConflict());
    }

    @Test
    void shouldNotUpdateEmptyName() throws Exception{
        Genre genre = makeExistingGenre();
        genre.setName(" ");

        String inputJson = generateJson(genre);

        var request = put("/api/genre/1")
                .header("Authorization", TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotUpdateDuplicateName() throws Exception{
        Genre genre = makeExistingGenre();
        Genre expected = makeExistingGenre();
        expected.setGenreId(5);

        Mockito.when(repository.findByName(genre.getName())).thenReturn(expected);

        String inputJson = generateJson(genre);

        var request = put("/api/genre/1")
                .header("Authorization", TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    //DELETE
    @Test
    void shouldDelete() throws Exception {
        Mockito.when(repository.deleteById(1)).thenReturn(true);
        mvc.perform(delete("/api/genre/1")
                .header("Authorization", TOKEN))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldNotDelete() throws Exception {
        Mockito.when(repository.deleteById(11)).thenReturn(false);
        mvc.perform(delete("/api/genre/11")
                .header("Authorization", TOKEN))
                .andExpect(status().isNotFound());
    }


    // Helper methods
    private String generateJson(Object o) throws JsonProcessingException {
        ObjectMapper jsonMapper = new JsonMapper();
        return jsonMapper.writeValueAsString(o);
    }

    private Genre makeExistingGenre(){
        return new Genre(1, "Fantasy", "Fantasy description");
    }

    private Genre makeNewGenre(){
        return new Genre(0, "Thriller", "Thriller description");
    }

    private final String TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJkZXYxMC11c2" +
                "Vycy1hcGkiLCJzdWIiOiJqb2huc21pdGgiLCJpZCI6Ijk4M2YxMjI0LWFmNGYtMTFlYi04MzY4LTAyNDJ" +
                "hYzExMDAwMiIsImZpcnN0X25hbWUiOiJKb2huIiwibGFzdF9uYW1lIjoiU21pdGgiLCJlbWFpbF9hZGRy" +
                "ZXNzIjoiam9obkBzbWl0aC5jb20iLCJtb2JpbGVfcGhvbmUiOiI1NTUtNTU1LTU1NTUiLCJyb2xlcyI6I" +
                "kFETUlOLE1BTkFHRVIsVVNFUiIsImV4cCI6MTYzMjM0MzI1Nn0.IrZkesm5Uc5Ei4Tmpdrbk9kaaIt6mlEydX7z9yKm3QY";
    }
