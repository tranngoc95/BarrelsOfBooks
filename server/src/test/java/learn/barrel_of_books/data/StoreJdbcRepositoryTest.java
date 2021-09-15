package learn.barrel_of_books.data;

import learn.barrel_of_books.models.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class StoreJdbcRepositoryTest {

    @Autowired
    StoreJdbcRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }



    @Test
    void shouldFindAllStores() {
        List<Store> stores = repository.findAll();
        assertTrue(stores.size() >= 1);
    }

    @Test
    void shouldFindById() {
        Store store = repository.findById(1);
        assertEquals("Greenfield", store.getCity());
    }

    @Test
    void shouldNotFindIfIdIsNonExistent() {
        Store store = repository.findById(500);
        assertNull(store);
    }

    @Test
    void shouldFindByPostCode() {
        Store store = repository.findByPostCode("43121");
        assertEquals("Bloomington", store.getCity());
    }

    @Test
    void shouldNotFindIfPostCodeIsNonExistent() {
        Store store = repository.findByPostCode("500000");
        assertNull(store);
    }

    @Test
    void shouldAdd() {
        List<Store> stores = repository.findAll();
        Store store = makeStore();
        store = repository.add(store);
        assertEquals(stores.size() + 1, repository.findAll().size());
    }

    @Test
    void shouldUpdate() {
        Store store = makeStore();
        store.setStoreId(1);
        boolean success = repository.update(store);
        assertTrue(success);
        assertEquals("111 baker st",repository.findById(1).getAddress());
    }

    @Test
    void shouldDelete() {
        List<Store> stores = repository.findAll();
        boolean success = repository.delete(3);
        assertTrue(success);
        assertEquals(stores.size() - 1, repository.findAll().size());
    }






    private Store makeStore() {
        Store store = new Store();
        store.setAddress("111 baker st");
        store.setCity("bakersville");
        store.setState("delaware");
        store.setPostalCode("12345");
        store.setPhone("12567");
        return store;
    }

}