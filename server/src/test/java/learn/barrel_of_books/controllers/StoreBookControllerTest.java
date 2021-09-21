package learn.barrel_of_books.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.sun.tools.jconsole.JConsoleContext;
import learn.barrel_of_books.data.StoreBookRepository;
import learn.barrel_of_books.models.Store;
import learn.barrel_of_books.models.StoreBook;
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
class StoreBookControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    StoreBookRepository repository;

    @Test
    void shouldFindByBookId() throws Exception {
        Store store = new Store(1,"111","boulder","CO","80301","2222222222");
        List<StoreBook> expected = new ArrayList<>();
        expected.add(makeExistingStoreBook());
        expected.add(new StoreBook(1,store,45));
        expected.add(new StoreBook(1,store,4));

        when(repository.findByBookId(1)).thenReturn(expected);

        String expectedJson = generateJson(expected);

        mvc.perform(get("/api/store-book/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }


    @Test
    void shouldAdd() throws Exception{
        StoreBook storeBook = makeNewStoreBook();
        when(repository.add(storeBook)).thenReturn(true);

        String inputJson = generateJson(storeBook);

        var request = post("/api/store-book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().json(inputJson));
    }

    @Test
    void shouldNotAddIfBookIdIs0() throws Exception{
        StoreBook storeBook = makeNewStoreBook();
        storeBook.setBookId(0);

        String inputJson = generateJson(storeBook);

        var request = post("/api/store-book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }


    @Test
    void shouldNotAddIfStoreIsNull() throws Exception{
        StoreBook storeBook = makeNewStoreBook();
        storeBook.setStore(null);

        String inputJson = generateJson(storeBook);

        var request = post("/api/store-book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }


    @Test
    void shouldUpdate() throws Exception {
        StoreBook storeBook = makeExistingStoreBook();
        when(repository.update(storeBook)).thenReturn(true);

        String jsn = generateJson(storeBook);

        var request = put("/api/store-book/1/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsn);

        mvc.perform(request)
                .andExpect(status().isNoContent());

    }

    @Test
    void shouldNotUpdateIfConflict() throws Exception {
        StoreBook storeBook = makeExistingStoreBook();
        when(repository.update(storeBook)).thenReturn(true);

        String jsn = generateJson(storeBook);

        var request = put("/api/store-book/11/11")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsn);

        mvc.perform(request)
                .andExpect(status().isConflict());

    }

    @Test
    void shouldDelete() throws Exception {
        when(repository.delete(1)).thenReturn(true);
        mvc.perform(delete("/api/store-book/1/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldNotDelete() throws Exception {
        when(repository.delete(1)).thenReturn(false);
        mvc.perform(delete("/api/store-book/1/1"))
                .andExpect(status().isNotFound());
    }







    private String generateJson(Object o) throws JsonProcessingException {
        ObjectMapper jsonMapper = new JsonMapper();
        return jsonMapper.writeValueAsString(o);
    }

    private StoreBook makeExistingStoreBook() {
        Store store = new Store(1,"111","boulder","CO","80301","2222222222");
        return new StoreBook(1,store,14);
    }


    private StoreBook makeNewStoreBook() {
        Store store = new Store(2,"222","boulder","CO","80301","2222222222");
        return new StoreBook(3,store,50);
    }
}