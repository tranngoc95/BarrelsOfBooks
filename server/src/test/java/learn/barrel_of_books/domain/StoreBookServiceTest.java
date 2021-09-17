package learn.barrel_of_books.domain;

import learn.barrel_of_books.data.StoreBookRepository;
import learn.barrel_of_books.models.Store;
import learn.barrel_of_books.models.StoreBook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class StoreBookServiceTest {

    @Autowired
    StoreBookService service;

    @MockBean
    StoreBookRepository repository;

    @Test
    void shouldAdd() {
        Store store = new Store(1, "address 1", "Greenfield", "WI", "12345", "12345678");
        StoreBook sb = new StoreBook(2, store,12);

        when(repository.add(sb)).thenReturn(true);
        Result<StoreBook> result = service.add(sb);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotAddIfBookIdIs0() {
        Store store = new Store(1, "address 1", "Greenfield", "WI", "12345", "12345678");
        StoreBook sb = new StoreBook(0, store,12);

        Result<StoreBook> result = service.add(sb);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddIfStoreIsMissing() {
        StoreBook sb = new StoreBook(0, null,12);
        Result<StoreBook> result = service.add(sb);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldUpdate() {
        Store store = new Store(1, "address 1", "Greenfield", "WI", "12345", "12345678");
        StoreBook sb = new StoreBook(1, store,12);
        when(repository.update(sb)).thenReturn(true);

        Result<StoreBook> result = service.update(sb);
        assertTrue(result.isSuccess());
    }




}