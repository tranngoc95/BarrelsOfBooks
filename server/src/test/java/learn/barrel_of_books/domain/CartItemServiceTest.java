package learn.barrel_of_books.domain;

import learn.barrel_of_books.data.CartItemRepository;
import learn.barrel_of_books.models.Book;
import learn.barrel_of_books.models.CartItem;
import learn.barrel_of_books.models.Genre;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class CartItemServiceTest {

    @MockBean
    CartItemRepository repository;

    @Autowired
    CartItemService service;

    // READ
    @Test
    void shouldFindByTransactionId() {
        List<CartItem> expected = new ArrayList<>();
        expected.add(makeExisting());
        expected.add(new CartItem(1, 1, "1", makeBook(), 2));

        Mockito.when(repository.findByTransactionId(1)).thenReturn(expected);

        assertEquals(expected, service.findByTransactionId(1));
    }

    @Test
    void shouldFindActiveByUserId() {
        List<CartItem> expected = new ArrayList<>();
        expected.add(makeExisting());
        expected.add(new CartItem(1, 1, "1", makeBook(), 2));

        Mockito.when(repository.findActiveByUserId("1")).thenReturn(expected);

        assertEquals(expected, service.findActiveByUserId("1"));
    }

    // CREATE
    @Test
    void shouldAddNew() {
        CartItem input = makeNew();
        CartItem expected = makeNew();
        expected.setCartItemId(4);

        Mockito.when(repository.add(input)).thenReturn(expected);

        Result<CartItem> actual = service.add(input);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(expected, actual.getPayload());
    }

    @Test
    void shouldAddUpdate() {
        CartItem input = makeNew();
        CartItem expected = makeNew();
        expected.setCartItemId(3);

        Mockito.when(repository.findActiveByUserIdAndBookId("1", 1)).thenReturn(expected);
        Mockito.when(repository.update(input)).thenReturn(true);

        Result<CartItem> actual = service.add(input);
        assertEquals(ResultType.SUCCESS, actual.getType());

        expected.setQuantity(2);
        assertEquals(expected, actual.getPayload());
    }

    @Test
    void shouldNotAddPresetID() {
        CartItem input = makeNew();
        input.setCartItemId(7);

        Result<CartItem> actual = service.add(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("id"));
    }

    @Test
    void shouldNotAddNull() {
        Result<CartItem> actual = service.add(null);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("null"));
    }

    @Test
    void shouldNotAddEmptyUserId() {
        CartItem input = makeNew();
        input.setUserId("");

        Result<CartItem> actual = service.add(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("userid"));

        input.setUserId(null);

        actual = service.add(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("userid"));
    }

    @Test
    void shouldNotAddNullBook() {
        CartItem input = makeNew();
        input.setBook(null);

        Result<CartItem> actual = service.add(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("book"));
    }

    @Test
    void shouldNotAddZeroQuantity() {
        CartItem input = makeNew();
        input.setQuantity(0);

        Result<CartItem> actual = service.add(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("quantity"));
    }

    // UPDATE
    @Test
    void shouldUpdate() {
        CartItem input = makeExisting();

        Mockito.when(repository.update(input)).thenReturn(true);

        Result<CartItem> actual = service.update(input);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }





    // helper methods
    private Book makeBook() {
        return new Book(1, 45, "hp", "magic",
                "jk rowling", new BigDecimal("13.45"), null);
    }

    private CartItem makeExisting(){
        Book book = makeBook();
        CartItem cartItem = new CartItem(2, 1, "1", book, 1);
        return cartItem;
    }

    private CartItem makeNew(){
        Book book = makeBook();
        CartItem cartItem = new CartItem(0, 0, "5", book, 1);
        return cartItem;
    }
}