package learn.barrel_of_books.data;

import learn.barrel_of_books.models.Store;
import learn.barrel_of_books.models.StoreBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class StoreBookJdbcRepositoryTest {

    @Autowired
    StoreBookJdbcRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }


    @Test
    void shouldFindByBookId() {
        List<StoreBook> list = repository.findByBookId(1);
        assertTrue(list.size() >= 1);
    }

    @Test
    void shouldNotFindIfBookIdDoesNotExist() {
        List<StoreBook> list = repository.findByBookId(11);
        assertNull(list);
    }


    @Test
    void shouldAdd() {
        Store store = new Store(1, "address 1", "Greenfield", "WI", "12345", "12345678");
        StoreBook sb = new StoreBook(2, store,12);
        boolean success = repository.add(sb);
        assertTrue(success);
    }


    @Test
    void shouldUpdate() {
        Store store = new Store(1, "address 1", "Greenfield", "WI", "12345", "12345678");
        StoreBook sb = new StoreBook(1, store,12);
        boolean success = repository.update(sb);
        assertTrue(success);
    }

    @Test
    void shouldDelete() {
        boolean success = repository.delete(2,3);
        assertTrue(success);
        assertNull(repository.findByBookId(3));
    }

    @Test
    void shouldNotDelete() {
        boolean success = repository.delete(4,4);
        assertFalse(success);
    }

}