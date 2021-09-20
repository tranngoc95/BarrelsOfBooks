package learn.barrel_of_books.domain;

import learn.barrel_of_books.data.CartItemRepository;
import learn.barrel_of_books.data.TransactionRepository;
import learn.barrel_of_books.models.Book;
import learn.barrel_of_books.models.CartItem;
import learn.barrel_of_books.models.Transaction;
import learn.barrel_of_books.models.TransactionStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static learn.barrel_of_books.data.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void shouldFindActiveByUserId() {
        List<CartItem> expected = new ArrayList<>();
        expected.add(makeExistingCartItem());
        expected.add(new CartItem(1, 1, "1", makeBook(), 2));

        Mockito.when(repository.findActiveByUserId("1")).thenReturn(expected);

//        assertEquals(expected, service.findCartActiveByUserId("1"));
    }

    @Test
    void shouldFindByCartItemId() {
        CartItem expected = makeExistingCartItem();
        Mockito.when(repository.findByCartItemId(2)).thenReturn(expected);
        assertEquals(expected, service.findByCartItemId(2));
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

        Mockito.when(repository.findActiveByUserIdAndBookId("5", 1)).thenReturn(expected);
        expected.setQuantity(2);
        Mockito.when(repository.update(expected)).thenReturn(true);

        Result<CartItem> actual = service.add(input);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(expected, actual.getPayload());
    }

    @Test
    void shouldNotAddUpdate() {
        CartItem input = makeNewCartItem();
        CartItem expected = makeNewCartItem();
        expected.setCartItemId(3);

        Mockito.when(repository.findActiveByUserIdAndBookId("5", 1)).thenReturn(expected);
        expected.setQuantity(2);
        Mockito.when(repository.update(expected)).thenReturn(false);

        Result<CartItem> actual = service.add(input);
        assertEquals(ResultType.NOT_FOUND, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("not found"));
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
        CartItem input = makeUpdateCartItem();

        Mockito.when(repository.update(input)).thenReturn(true);

        Result<CartItem> actual = service.update(input);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotUpdateNoId() {
        CartItem input = makeNewCartItem();

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
        CartItem input = makeUpdateCartItem();
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
        CartItem input = makeUpdateCartItem();
        CartItem found = makeUpdateCartItem();
        found.setCartItemId(5);

        Mockito.when(repository.findActiveByUserIdAndBookId(
                input.getUserId(), input.getBook().getBookId())).thenReturn(found);

        Result<CartItem> actual = service.update(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("duplicate"));
    }

    @Test
    void shouldNotUpdateOrderedCartItem() {
        CartItem input = makeExistingCartItem();

        Mockito.when(transactionRepository.findByTransactionId(1)).thenReturn(makeExistingTransaction());

        Result<CartItem> actual = service.update(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("ordered"));
    }

    @Test
    void shouldNotUpdateMissing() {
        CartItem input = makeUpdateCartItem();

        Result<CartItem> actual = service.update(input);
        assertEquals(ResultType.NOT_FOUND, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("not found"));
    }

    @Test
    void shouldNotUpdateOverAvailableQuantity() {
        CartItem input = makeUpdateCartItem();
        input.setQuantity(50);
        input.setTransactionId(0);

        Result<CartItem> actual = service.update(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("quantity"));
    }

    // DELETE
    @Test
    void shouldDeleteInCart() {
        Mockito.when(repository.findByCartItemId(3)).thenReturn(makeUpdateCartItem());
        Result<CartItem> actual = service.deleteById(3);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldDeleteOrdered() {
        Mockito.when(repository.findByCartItemId(2)).thenReturn(makeExistingCartItem());
        Mockito.when(transactionRepository.findByTransactionId(1)).thenReturn(makeExistingTransaction());
        Result<CartItem> actual = service.deleteById(2);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldDeleteOrderedWithMultipleItems() {
        Transaction transaction = makeExistingTransaction();
        List<CartItem> cartItems = new ArrayList<>();
        CartItem cartItem = makeExistingCartItem();
        cartItems.add(cartItem);
        cartItem.setCartItemId(5);
        cartItems.add(cartItem);
        transaction.setBooks(cartItems);
        Mockito.when(repository.findByCartItemId(2)).thenReturn(makeExistingCartItem());
        Mockito.when(transactionRepository.findByTransactionId(1)).thenReturn(makeExistingTransaction());
        Result<CartItem> actual = service.deleteById(2);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotDeleteShipped(){
        Mockito.when(repository.findByCartItemId(2)).thenReturn(makeExistingCartItem());
        Transaction transaction = makeExistingTransaction();
        transaction.setStatus(TransactionStatus.SHIPPED);
        Mockito.when(transactionRepository.findByTransactionId(1)).thenReturn(transaction);

        Result<CartItem> actual = service.deleteById(2);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("shipped"));
    }

    @Test
    void shouldNotDeleteMissing() {
        Result<CartItem> actual = service.deleteById(10);
        assertEquals(ResultType.NOT_FOUND, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("not found"));
    }
}