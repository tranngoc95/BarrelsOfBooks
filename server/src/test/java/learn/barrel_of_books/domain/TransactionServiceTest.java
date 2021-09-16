package learn.barrel_of_books.domain;

import learn.barrel_of_books.data.BookRepository;
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

        Mockito.when(repository.add(input)).thenReturn(expected);

        Result<Transaction> actual = service.add(input);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(expected, actual.getPayload());
    }
}