package learn.barrel_of_books.domain;

import learn.barrel_of_books.data.CartItemRepository;
import learn.barrel_of_books.models.Book;
import learn.barrel_of_books.models.CartItem;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        expected.setTransactionId(3);

        Mockito.when(repository.findActiveByUserIdAndBookId("5", 1)).thenReturn(expected);
        expected.setQuantity(2);
        Mockito.when(repository.update(expected)).thenReturn(true);

        Result<CartItem> actual = service.add(input);
        assertEquals(ResultType.SUCCESS, actual.getType());


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

    @Test
    void shouldNotUpdateNoId() {
        CartItem input = makeExisting();
        input.setCartItemId(0);

        Result<CartItem> actual = service.update(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("id"));
    }

    @Test
    void shouldNotUpdateNull() {
        Result<CartItem> actual = service.update(null);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("null"));
    }

    @Test
    void shouldNotUpdateEmptyUserId() {
        CartItem input = makeExisting();
        input.setUserId(" ");

        Result<CartItem> actual = service.update(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("userid"));

        input.setUserId(null);

        actual = service.update(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("userid"));
    }

    @Test
    void shouldNotUpdateDuplicateUserIdAndBook() {
        CartItem input = makeExisting();
        CartItem found = makeExisting();
        found.setCartItemId(5);

        Mockito.when(repository.findActiveByUserIdAndBookId(
                input.getUserId(), input.getBook().getBookId())).thenReturn(found);

        Result<CartItem> actual = service.update(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("duplicate"));
    }

    @Test
    void shouldNotUpdateMissing() {
        CartItem input = makeExisting();
        input.setCartItemId(10);

        Result<CartItem> actual = service.update(input);
        assertEquals(ResultType.NOT_FOUND, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("not found"));
    }

    // DELETE
    @Test
    void shouldDelete() {
        Mockito.when(repository.deleteById(4)).thenReturn(true);
        assertTrue(service.deleteById(4));
    }

    @Test
    void shouldNotDeleteMissing() {
        assertFalse(service.deleteById(10));
    }

    // helper methods
    private Book makeBook() {
        return new Book(1, 45, "hp", "magic",
                "jk rowling", new BigDecimal("13.45"), null);
    }

    private CartItem makeExisting(){
        Book book = makeBook();
        return new CartItem(2, 1, "1", book, 1);
    }

    private CartItem makeNew(){
        Book book = makeBook();
        return new CartItem(0, 0, "5", book, 1);
    }
}