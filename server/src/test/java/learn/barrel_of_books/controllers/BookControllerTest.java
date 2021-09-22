package learn.barrel_of_books.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import learn.barrel_of_books.data.BookRepository;
import learn.barrel_of_books.models.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @MockBean
    BookRepository repository;

    @Autowired
    MockMvc mvc;

    @Test
    void shouldFindAll() throws Exception {
        List<Book> expected = new ArrayList<>();
        expected.add(makeExistingBook());
        expected.add(new Book(2,15,"harry potter 2", "magic 2","jk rowling", new BigDecimal("13.45")));
        expected.add(new Book(3,15,"harry potter 3", "magic 3","jk rowling", new BigDecimal("13.45")));

        when(repository.findAll()).thenReturn(expected);

        String expectedJson = generateJson(expected);

        mvc.perform(get("/api/book"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }



    @Test
    void shouldFindById() throws Exception {
        Book book = makeExistingBook();

        when(repository.findById(1)).thenReturn(book);

        String expectedJson = generateJson(book);

        mvc.perform(get("/api/book/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void shouldFindByTitle() throws Exception {
        Book book = makeExistingBook();

        when(repository.findByTitle(book.getTitle())).thenReturn(book);

        String expectedJson = generateJson(book);

        mvc.perform(get("/api/book/title/harrypotter"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }


    @Test
    void shouldAdd() throws Exception {
        Book book = makeNewBook();
        Book expected = makeNewBook();
        expected.setBookId(4);
        when(repository.add(book)).thenReturn(expected);

        String expectedJson = generateJson(expected);
        String bookJson = generateJson(book);

        var request = post("/api/book")
                .header("Authorization", getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }


    @Test
    void shouldNotAddEmptyTitle() throws Exception {
        Book book = makeNewBook();
        book.setTitle("  ");
        String inputJson = generateJson(book);

        var request = post("/api/book")
                .header("Authorization", getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest());

    }

    @Test
    void shouldNotAddEmptyAuthor() throws Exception {
        Book book = makeNewBook();
        book.setAuthor("  ");
        String inputJson = generateJson(book);

        var request = post("/api/book")
                .header("Authorization", getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest());

    }

    @Test
    void shouldNotAddEmptyQuantity() throws Exception {
        Book book = makeNewBook();
        book.setQuantity(0);
        String inputJson = generateJson(book);

        var request = post("/api/book")
                .header("Authorization", getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest());

    }

    @Test
    void shouldNotAddEmptyPrice() throws Exception {
        Book book = makeNewBook();
        book.setPrice(BigDecimal.ZERO);
        String inputJson = generateJson(book);

        var request = post("/api/book")
                .header("Authorization", getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest());

    }
    @Test
    void shouldNotAddDuplicateAuthorAndTitle() throws Exception {
        Book book = makeExistingBook();

        Genre genre = new Genre(1, "Fantasy", "Fantasy description");
        GenreBook gb = new GenreBook(genre,1);
        List<GenreBook> listGb = new ArrayList<>();
        listGb.add(gb);
        book.setGenres(listGb);

        List<Book> expected = new ArrayList<>();
        List<StoreBook> sb = new ArrayList<>();
        expected.add(makeExistingBook());
        expected.add(new Book(2,15,"harry potter 2", "magic 2","jk rowling", new BigDecimal("13.45"),listGb,sb));
        expected.add(new Book(3,15,"harry potter 3", "magic 3","jk rowling", new BigDecimal("13.45"),listGb,sb));
        when(repository.findAll()).thenReturn(expected);

        String inputJson = generateJson(book);

        var request = post("/api/book")
                .header("Authorization", getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest());

    }

    @Test
    void shouldUpdate() throws Exception{
        Book book = makeExistingBook();
        Genre genre = new Genre(1, "Fantasy", "Fantasy description");
        GenreBook gb = new GenreBook(genre,1);
        List<GenreBook> listGb = new ArrayList<>();
        book.setGenres(listGb);
        listGb.add(gb);

        List<Book> expected = new ArrayList<>();
        Store store = new Store(1,"111","boulder","CO","80301","2222222222");
        StoreBook sb = new StoreBook(1,store,14);
        List<StoreBook> sbList = new ArrayList<>();
        sbList.add(sb);
        book.setStores(sbList);

        expected.add(makeExistingBook());
        expected.add(new Book(2,15,"harry potter 2", "magic 2","jk rowling", new BigDecimal("13.45"),listGb,sbList));
        expected.add(new Book(3,15,"harry potter 3", "magic 3","jk rowling", new BigDecimal("13.45"),listGb,sbList));


        book.setTitle("beep");
        when(repository.findAll()).thenReturn(expected);
        when(repository.update(book)).thenReturn(true);

        String inputJson = generateJson(book);

        var request = put("/api/book/1")
                .header("Authorization", getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isNoContent());

    }

    @Test
    void shouldNotUpdateIfConflict() throws Exception{
        List<Book> expected = new ArrayList<>();
        expected.add(makeExistingBook());
        List<StoreBook> sb = new ArrayList<>();
        List<GenreBook> gb = new ArrayList<>();
        expected.add(new Book(2,15,"harry potter 2", "magic 2","jk rowling", new BigDecimal("13.45"),gb,sb));
        expected.add(new Book(3,15,"harry potter 3", "magic 3","jk rowling", new BigDecimal("13.45"),gb,sb));

        Book book = makeExistingBook();
        book.setTitle("beep");
        when(repository.findAll()).thenReturn(expected);
        when(repository.update(book)).thenReturn(true);

        String inputJson = generateJson(book);

        var request = put("/api/book/10")
                .header("Authorization", getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isConflict());

    }

    @Test
    void shouldDelete() throws Exception {
        when(repository.delete(1)).thenReturn(true);
        mvc.perform(delete("/api/book/1")
                .header("Authorization", getToken()))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldNotDelete() throws Exception {
        when(repository.delete(11)).thenReturn(false);
        mvc.perform(delete("/api/book/11")
                .header("Authorization", getToken()))
                .andExpect(status().isNotFound());
    }




    private String generateJson(Object o) throws JsonProcessingException {
        ObjectMapper jsonMapper = new JsonMapper();
        return jsonMapper.writeValueAsString(o);
    }

    private String getToken() {
        return "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJkZXYxMC11c2" +
                "Vycy1hcGkiLCJzdWIiOiJqb2huc21pdGgiLCJpZCI6Ijk4M2YxMjI0LWFmNGYtMTFlYi04MzY4LTAyNDJ" +
                "hYzExMDAwMiIsImZpcnN0X25hbWUiOiJKb2huIiwibGFzdF9uYW1lIjoiU21pdGgiLCJlbWFpbF9hZGRy" +
                "ZXNzIjoiam9obkBzbWl0aC5jb20iLCJtb2JpbGVfcGhvbmUiOiI1NTUtNTU1LTU1NTUiLCJyb2xlcyI6I" +
                "kFETUlOLE1BTkFHRVIsVVNFUiIsImV4cCI6MTYzMjM0MzI1Nn0.IrZkesm5Uc5Ei4Tmpdrbk9kaaIt6mlEydX7z9yKm3QY";
    }

    private Book makeExistingBook() {
        List<StoreBook> sb = new ArrayList<>();
        List<GenreBook> gb = new ArrayList<>();
        return new Book(1,12,"harrypotter", "magic","jk rowling", new BigDecimal(13.45),gb,sb);
    }

    private Book makeNewBook() {
        Genre genre = new Genre(1, "Fantasy", "Fantasy description");
        GenreBook gb = new GenreBook(genre,1);
        List<GenreBook> listGb = new ArrayList<>();
        listGb.add(gb);

        Store store = new Store(1,"111","boulder","CO","80301","2222222222");
        StoreBook sb = new StoreBook(1,store,14);
        List<StoreBook> sbList = new ArrayList<>();
        sbList.add(sb);
        return new Book(0,12,"lord of the rings", "weird","random", new BigDecimal(17),listGb,sbList);
    }


}