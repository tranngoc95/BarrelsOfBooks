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

import java.util.List;

import static learn.barrel_of_books.data.TestData.makeExistingTransaction;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    // READ
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
    void shouldFindByTransactionId() throws Exception {
        Transaction expected = makeExistingTransaction();

        Mockito.when(repository.findByTransactionId(1)).thenReturn(expected);

        String expectedJson = generateJson(expected);

        mvc.perform(get("/api/transaction/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    // helper methods
    private String generateJson(Object o) throws JsonProcessingException {
        ObjectMapper jsonMapper = new JsonMapper();
        return jsonMapper.writeValueAsString(o);
    }

}