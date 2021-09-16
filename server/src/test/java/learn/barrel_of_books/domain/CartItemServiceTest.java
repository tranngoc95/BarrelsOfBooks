package learn.barrel_of_books.domain;

import learn.barrel_of_books.data.CartItemRepository;
import learn.barrel_of_books.data.TransactionRepository;
import learn.barrel_of_books.models.CartItem;
import learn.barrel_of_books.models.Transaction;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static learn.barrel_of_books.data.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class CartItemServiceTest {

    @MockBean
    CartItemRepository repository;

    @MockBean
    TransactionRepository transactionRepository;

    @Autowired
    CartItemService service;

    // READ
    @Test
    void shouldFindByTransactionId() {
        List<CartItem> expected = new ArrayList<>();
        expected.add(makeExistingCartItem());
        expected.add(new CartItem(1, 1, "1", makeBook(), 2));

        Mockito.when(repository.findByTransactionId(1)).thenReturn(expected);

        assertEquals(expected, service.findByTransactionId(1));
    }

    @Test
    void shouldFindActiveByUserId() {
        List<CartItem> expected = new ArrayList<>();
        expected.add(makeExistingCartItem());
        expected.add(new CartItem(1, 1, "1", makeBook(), 2));

        Mockito.when(repository.findActiveByUserId("1")).thenReturn(expected);

        assertEquals(expected, service.findActiveByUserId("1"));
    }

    // CREATE
    @Test
    void shouldAddNew() {
        CartItem input = makeNewCartItem();
        CartItem expected = makeNewCartItem();
        expected.setCartItemId(4);

        Mockito.when(repository.add(input)).thenReturn(expected);

        Result<CartItem> actual = service.add(input);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(expected, actual.getPayload());
    }

    @Test
    void shouldAddUpdate() {
        CartItem input = makeNewCartItem();
        CartItem expected = makeNewCartItem();
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
        CartItem input = makeNewCartItem();
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
        CartItem input = makeNewCartItem();
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
        CartItem input = makeNewCartItem();
        input.setBook(null);

        Result<CartItem> actual = service.add(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("book"));
    }

    @Test
    void shouldNotAddZeroQuantity() {
        CartItem input = makeNewCartItem();
        input.setQuantity(0);

        Result<CartItem> actual = service.add(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("quantity"));
    }

    @Test
    void shouldNotAddConflictUserId() {
        CartItem input = makeNewCartItem();
        input.setTransactionId(6);
        Transaction transaction = makeExistingTransaction();
        transaction.setTransactionId(6);

        Mockito.when(transactionRepository.findByTransactionId(6)).thenReturn(transaction);

        Result<CartItem> actual = service.add(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("userid"));
    }

    @Test
    void shouldNotAddOverAvailableQuantity() {
        CartItem input = makeNewCartItem();
        input.setQuantity(50);

        Result<CartItem> actual = service.add(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("quantity"));
    }

    // UPDATE
    @Test
    void shouldUpdate() {
        CartItem input = makeExistingCartItem();

        Mockito.when(repository.update(input)).thenReturn(true);

        Result<CartItem> actual = service.update(input);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotUpdateNoId() {
        CartItem input = makeExistingCartItem();
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
        CartItem input = makeExistingCartItem();
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
        CartItem input = makeExistingCartItem();
        CartItem found = makeExistingCartItem();
        found.setCartItemId(5);

        Mockito.when(repository.findActiveByUserIdAndBookId(
                input.getUserId(), input.getBook().getBookId())).thenReturn(found);

        Result<CartItem> actual = service.update(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("duplicate"));
    }

    @Test
    void shouldNotUpdateMissing() {
        CartItem input = makeExistingCartItem();
        input.setCartItemId(10);

        Result<CartItem> actual = service.update(input);
        assertEquals(ResultType.NOT_FOUND, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("not found"));
    }

    @Test
    void shouldNotUpdateConflictUserId() {
        CartItem input = makeExistingCartItem();
        input.setUserId("4");
        Transaction transaction = makeExistingTransaction();

        Mockito.when(transactionRepository.findByTransactionId(1)).thenReturn(transaction);

        Result<CartItem> actual = service.add(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("userid"));
    }

    @Test
    void shouldNotUpdateOverAvailableQuantity() {
        CartItem input = makeExistingCartItem();
        input.setQuantity(50);
        input.setTransactionId(0);

        Result<CartItem> actual = service.update(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("quantity"));
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
}