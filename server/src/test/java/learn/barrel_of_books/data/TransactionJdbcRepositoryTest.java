package learn.barrel_of_books.data;

import learn.barrel_of_books.models.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static learn.barrel_of_books.data.TestData.makeExistingTransaction;
import static learn.barrel_of_books.data.TestData.makeNewTransaction;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class TransactionJdbcRepositoryTest {
    @Autowired
    TransactionRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindByUserId() {
        List<Transaction> actual = repository.findByUserId("1");
        assertNotNull(actual);
        assertTrue(actual.size()>=1);
    }

    @Test
    void shouldFindByTransactionId() {
        Transaction actual = repository.findByTransactionId(1);
        assertNotNull(actual);
        assertEquals("1", actual.getUserId());
    }

    @Test
    void shouldAdd() {
        Transaction actual = repository.add(makeNewTransaction());
        assertNotNull(actual);
        assertEquals(4, actual.getTransactionId());
    }

    @Test
    void shouldUpdate() {
        Transaction input = makeExistingTransaction();
        input.setDate(LocalDate.now());
        assertTrue(repository.update(input));
    }

    @Test
    void shouldNotUpdateMissing() {
        Transaction input = makeExistingTransaction();
        input.setTransactionId(10);
        assertFalse(repository.update(input));
    }

    @Test
    void shouldDelete() {
        assertTrue(repository.deleteById(3));
    }

    @Test
    void shouldNotDeleteMissing() {
        assertFalse(repository.deleteById(10));
    }
}