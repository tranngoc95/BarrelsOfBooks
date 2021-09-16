package learn.barrel_of_books.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import learn.barrel_of_books.data.CartItemRepository;
import learn.barrel_of_books.models.CartItem;
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

import static learn.barrel_of_books.data.TestData.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CartItemControllerTest {
    @MockBean
    CartItemRepository repository;

    @Autowired
    MockMvc mvc;

    // READ
    @Test
    void shouldFindActiveByUserId() throws Exception {
        List<CartItem> expected = new ArrayList<>();
        expected.add(makeExistingCartItem());
        expected.add(new CartItem(1, 1, "1", makeBook(), 2));

        Mockito.when(repository.findActiveByUserId("1")).thenReturn(expected);

        String expectedJson = generateJson(expected);

        mvc.perform(get("/api/cart-item/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    // CREATE
    @Test
    void shouldAdd() throws Exception {
        CartItem input = makeNewCartItem();
        CartItem expected = makeNewCartItem();
        expected.setCartItemId(5);

        Mockito.when(repository.add(input)).thenReturn(expected);

        String inputJson = generateJson(input);
        String expectedJson = generateJson(expected);

        var request = post("/api/cart-item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void shouldNotAddEmptyUserId() throws Exception{
        CartItem input = makeNewCartItem();
        input.setUserId(" ");

        String inputJson = generateJson(input);

        var request = post("/api/cart-item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotAddPresetId() throws Exception{
        CartItem input = makeExistingCartItem();

        String inputJson = generateJson(input);

        var request = post("/api/cart-item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    //UPDATE
    @Test
    void shouldUpdate() throws Exception {
        CartItem input = makeExistingCartItem();

        Mockito.when(repository.update(input)).thenReturn(true);

        String inputJson = generateJson(input);

        var request = put("/api/cart-item/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request).andExpect(status().isNoContent());
    }

    @Test
    void shouldNotUpdateConflict() throws Exception {
        CartItem input = makeExistingCartItem();

        String inputJson = generateJson(input);

        var request = put("/api/cart-item/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request).andExpect(status().isConflict());
    }

    @Test
    void shouldNotUpdateEmptyUserId() throws Exception{
        CartItem input = makeExistingCartItem();
        input.setUserId(" ");

        String inputJson = generateJson(input);

        var request = put("/api/cart-item/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotUpdateNoId() throws Exception{
        CartItem input = makeNewCartItem();

        String inputJson = generateJson(input);

        var request = put("/api/cart-item/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    //DELETE
    @Test
    void shouldDelete() throws Exception {
        Mockito.when(repository.deleteById(1)).thenReturn(true);
        mvc.perform(delete("/api/cart-item/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldNotDelete() throws Exception {
        Mockito.when(repository.deleteById(11)).thenReturn(false);
        mvc.perform(delete("/api/cart-item/11"))
                .andExpect(status().isNotFound());
    }


    // helper methods
    private String generateJson(Object o) throws JsonProcessingException {
        ObjectMapper jsonMapper = new JsonMapper();
        return jsonMapper.writeValueAsString(o);
    }

}