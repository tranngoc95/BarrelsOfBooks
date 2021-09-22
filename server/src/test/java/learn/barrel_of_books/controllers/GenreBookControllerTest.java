package learn.barrel_of_books.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import learn.barrel_of_books.data.GenreBookRepository;
import learn.barrel_of_books.models.Genre;
import learn.barrel_of_books.models.GenreBook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest
@AutoConfigureMockMvc
class GenreBookControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    GenreBookRepository repository;

    @Test
    void shouldFindByBookId() throws Exception {
        Genre genre2 = new Genre(2,"fiction", "not real");
        Genre genre3 = new Genre(3,"fiction", "not real");
        List<GenreBook> expected = new ArrayList<>();
        expected.add(makeGenreBook());
        expected.add(new GenreBook(genre2,1));
        expected.add(new GenreBook(genre3,1));

        when(repository.findByBookId(1)).thenReturn(expected);

        String expectedJson = generateJson(expected);

        mvc.perform(get("/api/genre-book/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

    }

    @Test
    void shouldAdd() throws Exception {
        GenreBook genreBook = makeGenreBook();
        when(repository.add(genreBook)).thenReturn(true);

        String jsn = generateJson(genreBook);

        var request = post("/api/genre-book")
                .header("Authorization", getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsn);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().json(jsn));
    }

    @Test
    void shouldNotAddIfBookIdIs0() throws Exception {
        GenreBook genreBook = makeGenreBook();
        genreBook.setBookId(0);
        when(repository.add(genreBook)).thenReturn(true);

        String jsn = generateJson(genreBook);

        var request = post("/api/genre-book")
                .header("Authorization", getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsn);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotAddIfGenreIsNull() throws Exception {
        GenreBook genreBook = makeGenreBook();
        genreBook.setGenre(null);
        when(repository.add(genreBook)).thenReturn(true);

        String jsn = generateJson(genreBook);

        var request = post("/api/genre-book")
                .header("Authorization", getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsn);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDelete() throws Exception {
        when(repository.delete(1)).thenReturn(true);
        mvc.perform(delete("/api/genre-book/1")
                .header("Authorization", getToken()))
                .andExpect(status().isNoContent());
    }
    @Test
    void shouldNotDelete() throws Exception {
        when(repository.delete(1)).thenReturn(false);
        mvc.perform(delete("/api/genre-book/1")
                .header("Authorization", getToken()))
                .andExpect(status().isNotFound());
    }


    // Helper methods
    private String generateJson(Object o) throws JsonProcessingException {
        ObjectMapper jsonMapper = new JsonMapper();
        return jsonMapper.writeValueAsString(o);
    }


    private GenreBook makeGenreBook() {
        Genre genre = new Genre(1,"fiction", "not real");
        return new GenreBook(genre,1);
    }

    private String getToken() {
        return "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJkZXYxMC11c2" +
                "Vycy1hcGkiLCJzdWIiOiJqb2huc21pdGgiLCJpZCI6Ijk4M2YxMjI0LWFmNGYtMTFlYi04MzY4LTAyNDJ" +
                "hYzExMDAwMiIsImZpcnN0X25hbWUiOiJKb2huIiwibGFzdF9uYW1lIjoiU21pdGgiLCJlbWFpbF9hZGRy" +
                "ZXNzIjoiam9obkBzbWl0aC5jb20iLCJtb2JpbGVfcGhvbmUiOiI1NTUtNTU1LTU1NTUiLCJyb2xlcyI6I" +
                "kFETUlOLE1BTkFHRVIsVVNFUiIsImV4cCI6MTYzMjM0MzI1Nn0.IrZkesm5Uc5Ei4Tmpdrbk9kaaIt6mlEydX7z9yKm3QY";
    }
}