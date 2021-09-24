package learn.barrel_of_books.domain;

import learn.barrel_of_books.data.BookRepository;
import learn.barrel_of_books.data.CartItemRepository;
import learn.barrel_of_books.data.TransactionRepository;
import learn.barrel_of_books.models.CartItem;
import learn.barrel_of_books.models.Transaction;
import learn.barrel_of_books.models.TransactionStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static learn.barrel_of_books.data.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class TransactionServiceTest {

    @MockBean
    TransactionRepository repository;

    @MockBean
    CartItemRepository cartItemRepository;

    @MockBean
    BookRepository bookRepository;

    @Autowired
    TransactionService service;

    // READ
    @Test
    void shouldAll() {
        List<Transaction> expected = new ArrayList<>();
        expected.add(makeExistingTransaction());

        Mockito.when(repository.findAll()).thenReturn(expected);

        assertEquals(expected, service.findAll());
    }

    @Test
    void shouldFindByUserId() {
        List<Transaction> expected = new ArrayList<>();
        expected.add(makeExistingTransaction());

        Mockito.when(repository.findByUserId("1")).thenReturn(expected);

        assertEquals(expected, service.findByUserId("1"));
    }

    @Test
    void shouldFindByTransactionId() {
        Transaction expected = makeExistingTransaction();

        Mockito.when(repository.findByTransactionId(1)).thenReturn(expected);

        assertEquals(expected, service.findByTransactionId(1));
    }

    // CREATE
    @Test
    void shouldAddNew() {
        Transaction input = makeNewTransaction();
        Transaction expected = makeNewTransaction();
        expected.setTransactionId(4);

        Mockito.when(cartItemRepository.findByCartItemId(1)).thenReturn(expected.getBooks().get(0));
        Mockito.when(repository.add(input)).thenReturn(expected);

        Result<Transaction> actual = service.add(input);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(expected, actual.getPayload());
    }

    @Test
    void shouldNotAddPresetID() {
        Transaction input = makeNewTransaction();
        input.setTransactionId(7);

        Result<Transaction> actual = service.add(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("id"));
    }

    @Test
    void shouldNotAddNull() {
        Result<Transaction> actual = service.add(null);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("null"));
    }

    @Test
    void shouldNotAddEmptyBooks() {
        Transaction input = makeNewTransaction();
        input.setBooks(null);

        Result<Transaction> actual = service.add(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("book"));

        input.setBooks(new ArrayList<>());

        actual = service.add(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("book"));
    }

    @Test
    void shouldNotAddEmptyUserId() {
        Transaction input = makeNewTransaction();
        input.setUserId(" ");

        Result<Transaction> actual = service.add(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("userid"));

        input.setUserId(null);

        actual = service.add(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("userid"));
    }

    @Test
    void shouldNotAddFutureDate() {
        Transaction input = makeNewTransaction();
        input.setDate(LocalDate.now().plusDays(2));

        Result<Transaction> actual = service.add(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("date"));
    }

    @Test
    void shouldNotAddOverQuantity() {
        CartItem item = makeExistingCartItem();
        item.setQuantity(50);
        Transaction input = makeNewTransaction();
        input.setBooks(List.of(item));

        Result<Transaction> actual = service.add(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("quantity"));
    }

    @Test
    void shouldNotAddConflictUserId() {
        Transaction input = makeNewTransaction();
        input.setUserId("5");

        Result<Transaction> actual = service.add(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("userid"));
    }

    @Test
    void shouldNotAddCartItemDoesntMatchInventory() {
        Transaction input = makeNewTransaction();

        Result<Transaction> actual = service.add(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("inventory"));
    }

    @Test
    void shouldNotAddPresetTotal() {
        Transaction input = makeNewTransaction();
        input.setTotal(BigDecimal.ONE);

        Result<Transaction> actual = service.add(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("total"));
    }

    // UPDATE
    @Test
    void shouldUpdate() {
        Transaction input = makeExistingTransaction();

        Mockito.when(repository.findByTransactionId(1)).thenReturn(input);
        Mockito.when(repository.update(input)).thenReturn(true);

        Result<Transaction> actual = service.update(input);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotUpdateNoId() {
        Transaction input = makeNewTransaction();

        Mockito.when(repository.findByTransactionId(1)).thenReturn(input);

        Result<Transaction> actual = service.update(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("id"));
    }

    @Test
    void shouldNotUpdateNull() {
        Result<Transaction> actual = service.update(null);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("null"));
    }

    @Test
    void shouldNotUpdateEmptyUserId() {
        Transaction input = makeExistingTransaction();
        input.setUserId(" ");

        Result<Transaction> actual = service.update(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("userid"));

        input.setUserId(null);

        actual = service.update(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("userid"));
    }

    @Test
    void shouldNotUpdateEmptyBook() {
        Transaction input = makeExistingTransaction();
        input.setBooks(null);

        Result<Transaction> actual = service.update(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("book"));

        input.setBooks(new ArrayList<>());

        actual = service.update(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("book"));
    }

    @Test
    void shouldNotUpdateNullDate() {
        Transaction input = makeExistingTransaction();
        input.setDate(null);
        Mockito.when(repository.findByTransactionId(1)).thenReturn(input);

        Result<Transaction> actual = service.update(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("date"));
    }

    @Test
    void shouldNotUpdateFutureDate() {
        Transaction input = makeExistingTransaction();
        input.setDate(LocalDate.now().plusDays(2));

        Result<Transaction> actual = service.update(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("date"));
    }

    @Test
    void shouldNotUpdateConflictUserId() {
        Transaction input = makeExistingTransaction();
        input.setUserId("10");

        Result<Transaction> actual = service.update(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("userid"));
    }

    @Test
    void shouldNotUpdateEmptyBooks() {
        Transaction input = makeExistingTransaction();
        Transaction other = makeExistingTransaction();
        other.setBooks(null);
        Mockito.when(repository.findByTransactionId(1)).thenReturn(other);

        Result<Transaction> actual = service.update(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("book"));
    }

    @Test
    void shouldNotUpdateMissing() {
        Transaction input = makeExistingTransaction();

        Result<Transaction> actual = service.update(input);
        assertEquals(ResultType.NOT_FOUND, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("not found"));
    }

    // DELETE
    @Test
    void shouldDelete() {
        Mockito.when(repository.findByTransactionId(1)).thenReturn(makeExistingTransaction());
        Mockito.when(repository.deleteById(1)).thenReturn(true);
        Result<Transaction> actual = service.deleteById(1);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotDeleteMissing() {
        Result<Transaction> actual = service.deleteById(10);
        assertEquals(ResultType.NOT_FOUND, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("not found"));
    }

    @Test
    void shouldNotDeleteShipped() {
        Transaction transaction = makeExistingTransaction();
        transaction.setStatus(TransactionStatus.SHIPPED);
        Mockito.when(repository.findByTransactionId(1)).thenReturn(transaction);

        Result<Transaction> actual = service.deleteById(1);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("shipped"));
    }

}