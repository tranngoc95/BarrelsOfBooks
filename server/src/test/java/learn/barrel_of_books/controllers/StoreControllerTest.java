package learn.barrel_of_books.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import learn.barrel_of_books.data.StoreRepository;
import learn.barrel_of_books.models.Genre;
import learn.barrel_of_books.models.Store;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StoreControllerTest {


    @MockBean
    StoreRepository repository;

    @Autowired
    MockMvc mvc;


    @Test
    void shouldFindAll() throws Exception {
        List<Store> expected = new ArrayList<>();
        expected.add(makeExistingStore());
        expected.add(new Store(2,"mary rd","boulder","CO","80301","1111111111"));
        expected.add(new Store(3,"miles dr","boulder","CO","80301","2222222222"));


        when(repository.findAll()).thenReturn(expected);

        String expectedJson = generateJson(expected);


        mvc.perform(get("/api/store"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }


    @Test
    void shouldFindById() throws Exception{
        Store store = makeExistingStore();

        when(repository.findById(1)).thenReturn(store);

        String expectedJson = generateJson(store);

        mvc.perform(get("/api/store/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }


    @Test
    void shouldAdd() throws Exception {
        Store store = makeNewStore();
        Store expected = makeNewStore();
        expected.setStoreId(4);
        when(repository.add(store)).thenReturn(expected);

        String inputJson = generateJson(store);
        String expectedJson = generateJson(expected);

        var request = post("/api/store")
                .header("Authorization", TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

    }

    @Test
    void shouldNotAddEmptyAddress() throws Exception{
        Store store = makeNewStore();
        store.setAddress(" ");

        String inputJson = generateJson(store);

        var request = post("/api/store")
                .header("Authorization", TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotAddEmptyCity() throws Exception{
        Store store = makeNewStore();
        store.setCity(" ");

        String inputJson = generateJson(store);

        var request = post("/api/store")
                .header("Authorization", TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotAddEmptyState() throws Exception{
        Store store = makeNewStore();
        store.setState(" ");

        String inputJson = generateJson(store);

        var request = post("/api/store")
                .header("Authorization", TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotAddEmptyPostCode() throws Exception{
        Store store = makeNewStore();
        store.setPostalCode(" ");

        String inputJson = generateJson(store);

        var request = post("/api/store")
                .header("Authorization", TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotAddEmptyPhone() throws Exception{
        Store store = makeNewStore();
        store.setPhone(" ");

        String inputJson = generateJson(store);

        var request = post("/api/store")
                .header("Authorization", TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotAddDuplicateAddressAndPostCode() throws Exception{
        List<Store> expected = new ArrayList<>();
        expected.add(makeExistingStore());
        expected.add(new Store(2,"mary rd","boulder","CO","80301","1111111111"));
        expected.add(new Store(3,"miles dr","boulder","CO","80301","2222222222"));

        Store store = makeExistingStore();

        when(repository.findAll()).thenReturn(expected);

        String inputJson = generateJson(store);

        var request = post("/api/store")
                .header("Authorization", TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdate() throws Exception {
        List<Store> expected = new ArrayList<>();
        expected.add(makeExistingStore());
        expected.add(new Store(2,"mary rd","boulder","CO","80301","1111111111"));
        expected.add(new Store(3,"miles dr","boulder","CO","80301","2222222222"));

        Store store = makeExistingStore();
        store.setAddress("777 heaven st");
        when(repository.update(store)).thenReturn(true);
        when(repository.findAll()).thenReturn(expected);

        String storeJson = generateJson(store);

        var request = put("/api/store/1")
                .header("Authorization", TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(storeJson);

        mvc.perform(request).andExpect(status().isNoContent());

    }

    @Test
    void shouldNotUpdateIfConflict() throws Exception {
        List<Store> expected = new ArrayList<>();
        expected.add(makeExistingStore());
        expected.add(new Store(2,"mary rd","boulder","CO","80301","1111111111"));
        expected.add(new Store(3,"miles dr","boulder","CO","80301","2222222222"));

        Store store = makeExistingStore();
        store.setAddress("777 heaven st");
        when(repository.update(store)).thenReturn(true);
        when(repository.findAll()).thenReturn(expected);

        String storeJson = generateJson(store);

        var request = put("/api/store/2")
                .header("Authorization", TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(storeJson);

        mvc.perform(request).andExpect(status().isConflict());

    }

    @Test
    void shouldDelete() throws Exception {
        when(repository.delete(1)).thenReturn(true);
        mvc.perform(delete("/api/store/1")
                .header("Authorization", TOKEN))
                .andExpect(status().isNoContent());
    }


    @Test
    void shouldNotDelete() throws Exception {
        when(repository.delete(11)).thenReturn(false);
        mvc.perform(delete("/api/store/11")
                .header("Authorization", TOKEN))
                .andExpect(status().isNotFound());
    }

    // Helper methods
    private String generateJson(Object o) throws JsonProcessingException {
        ObjectMapper jsonMapper = new JsonMapper();
        return jsonMapper.writeValueAsString(o);
    }

    private Store makeExistingStore() {
        return new Store(1,"111 birds dr","dundee","IL","60102","2243256666");
    }

    private Store makeNewStore() {
        return new Store(0,"222 new store","new york","NY","00000","5555555555");
    }

    public static final String TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJkZXYxMC11c2" +
            "Vycy1hcGkiLCJzdWIiOiJqb2huc21pdGgiLCJpZCI6Ijk4M2YxMjI0LWFmNGYtMTFlYi04MzY4LTAyNDJ" +
            "hYzExMDAwMiIsImZpcnN0X25hbWUiOiJKb2huIiwibGFzdF9uYW1lIjoiU21pdGgiLCJlbWFpbF9hZGRy" +
            "ZXNzIjoiam9obkBzbWl0aC5jb20iLCJtb2JpbGVfcGhvbmUiOiI1NTUtNTU1LTU1NTUiLCJyb2xlcyI6I" +
            "kFETUlOLE1BTkFHRVIsVVNFUiIsImV4cCI6MTYzMjM0MzI1Nn0.IrZkesm5Uc5Ei4Tmpdrbk9kaaIt6mlEydX7z9yKm3QY";
}