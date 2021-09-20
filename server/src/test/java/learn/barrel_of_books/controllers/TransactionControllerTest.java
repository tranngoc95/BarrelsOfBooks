package learn.barrel_of_books.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import learn.barrel_of_books.data.CartItemRepository;
import learn.barrel_of_books.data.TransactionRepository;
import learn.barrel_of_books.models.Transaction;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static learn.barrel_of_books.data.TestData.makeExistingTransaction;
import static learn.barrel_of_books.data.TestData.makeNewTransaction;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {
    @MockBean
    TransactionRepository repository;

    @MockBean
    CartItemRepository cartItemRepository;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    // READ
    @Test
    void shouldFindAll() throws Exception {
        List<Transaction> expected = List.of(makeExistingTransaction());

        Mockito.when(repository.findAll()).thenReturn(expected);

        String expectedJson = generateJson(expected);

        mvc.perform(get("/api/transaction"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void shouldFindByUserId() throws Exception {
        List<Transaction> expected = List.of(makeExistingTransaction());

        Mockito.when(repository.findByUserId("1")).thenReturn(expected);

        String expectedJson = generateJson(expected);

        mvc.perform(get("/api/transaction/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void shouldNotFindByTransactionId() throws Exception {
        mvc.perform(get("/api/transaction/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldFindByTransactionId() throws Exception {
        Transaction expected = makeExistingTransaction();

        Mockito.when(repository.findByTransactionId(1)).thenReturn(expected);

        String expectedJson = generateJson(expected);

        mvc.perform(get("/api/transaction/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    // CREATE
    @Test
    void shouldAdd() throws Exception {
        Transaction input = makeNewTransaction();
        Transaction expected = makeNewTransaction();
        expected.setTransactionId(5);
        expected.getBooks().get(0).getBook().subtractQuantity(2);
        expected.getBooks().get(0).setTransactionId(5);

        Transaction repositoryInput = makeNewTransaction();
        repositoryInput.updateTotal();
        repositoryInput.setDate(LocalDate.now());
        Transaction repositoryOutput = makeNewTransaction();
        repositoryOutput.setTransactionId(5);

        Mockito.when(cartItemRepository.findByCartItemId(1)).thenReturn(input.getBooks().get(0));
        Mockito.when(repository.add(repositoryInput)).thenReturn(repositoryOutput);

        String inputJson = generateJson(input);
        String expectedJson = generateJson(expected);

        var request = post("/api/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void shouldNotAddNoUserId() throws Exception {
        Transaction input = makeNewTransaction();
        input.setUserId(" ");

        String inputJson = generateJson(input);

        var request = post("/api/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotAddPresetId() throws Exception{
        Transaction input = makeExistingTransaction();

        String inputJson = generateJson(input);

        var request = post("/api/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    // UPDATE
    @Test
    void shouldUpdate() throws Exception {
        Transaction input = makeExistingTransaction();

        Mockito.when(repository.findByTransactionId(1)).thenReturn(input);
        Mockito.when(repository.update(input)).thenReturn(true);

        String inputJson = generateJson(input);

        var request = put("/api/transaction/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request).andExpect(status().isNoContent());
    }

    @Test
    void shouldNotUpdateIdConflict() throws Exception {
        Transaction input = makeExistingTransaction();

        String inputJson = generateJson(input);

        var request = put("/api/transaction/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isConflict());
    }

    @Test
    void shouldNotUpdateNoUserId() throws Exception{
        Transaction input = makeExistingTransaction();
        input.setUserId(" ");
        Mockito.when(repository.findByTransactionId(1)).thenReturn(input);

        String inputJson = generateJson(input);

        var request = put("/api/transaction/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotUpdateNoId() throws Exception {
        Transaction input = makeExistingTransaction();
        input.setTransactionId(0);

        String inputJson = generateJson(input);

        var request = put("/api/transaction/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    // DELETE
    @Test
    void shouldDelete() throws Exception {
        Mockito.when(repository.findByTransactionId(1)).thenReturn(makeExistingTransaction());
        Mockito.when(repository.deleteById(1)).thenReturn(true);
        mvc.perform(delete("/api/transaction/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldNotDelete() throws Exception {
        mvc.perform(delete("/api/transaction/11"))
                .andExpect(status().isNotFound());
    }

    // helper methods
    private String generateJson(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }

}