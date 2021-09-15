package learn.barrel_of_books.domain;

import learn.barrel_of_books.data.StoreJdbcRepository;
import learn.barrel_of_books.data.StoreRepository;
import learn.barrel_of_books.models.Store;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class StoreServiceTest {

    @Autowired
    StoreService service;

    @MockBean
    StoreRepository repository;


    @Test
    void shouldAddStore() {
        Store store = makeStore();
        when(repository.findAll()).thenReturn(getStoreList());
        when(repository.add(store)).thenReturn(store);
        Result<Store> result = service.add(store);
        assertTrue(result.isSuccess());
        assertEquals("111 baker st",result.getPayload().getAddress());
    }

    @Test
    void shouldNotAddIfStoreIsNull() {
        Store store = null;
        Result<Store> result = service.add(store);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddIfStoreIdIsSet() {
        Store store = makeStore();
        store.setStoreId(5);
        Result<Store> result = service.add(store);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddIfAddressIsBlank() {
        Store store = makeStore();
        store.setAddress("   ");
        Result<Store> result = service.add(store);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddIfCityIsBlank() {
        Store store = makeStore();
        store.setCity("   ");
        Result<Store> result = service.add(store);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddIfStateIsBlank() {
        Store store = makeStore();
        store.setState("   ");
        Result<Store> result = service.add(store);
        assertFalse(result.isSuccess());
    }


    @Test
    void shouldNotAddIfPostCodeIsBlank() {
        Store store = makeStore();
        store.setPostalCode("   ");
        Result<Store> result = service.add(store);
        assertFalse(result.isSuccess());
    }


    @Test
    void shouldNotAddIfPhoneIsBlank() {
        Store store = makeStore();
        store.setPhone("   ");
        Result<Store> result = service.add(store);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddIfDuplicatedPostCodeAndAddress() {
        Store store = makeStore();
        when(repository.findAll()).thenReturn(getStoreList());
        store.setAddress("aaa");
        store.setPostalCode("aaaaa");
        Result<Store> result = service.add(store);
        assertFalse(result.isSuccess());
    }


    @Test
    void shouldUpdate() {
        Store store = makeStore();
        store.setStoreId(1);
        when(repository.findAll()).thenReturn(getStoreList());
        when(repository.update(store)).thenReturn(true);
        Result<Store> result = service.update(store);
        assertTrue(result.isSuccess());
    }


    @Test
    void shouldNotUpdateIfIdIs0() {
        Store store = makeStore();
        when(repository.findAll()).thenReturn(getStoreList());
        when(repository.update(store)).thenReturn(true);
        store.setStoreId(0);
        Result<Store> result = service.update(store);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldUpdateWithoutChangingAddressAndPostCode() {
        Store store = makeStore();
        store.setStoreId(1);
        when(repository.findAll()).thenReturn(getStoreList());
        when(repository.update(store)).thenReturn(true);
        store.setAddress("aaa");
        store.setPostalCode("aaaaa");
        Result<Store> result = service.update(store);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotUpdateWithDuplicatedAddressAndPostCode() {
        Store store = makeStore();
        store.setStoreId(2);
        when(repository.findAll()).thenReturn(getStoreList());
        when(repository.update(store)).thenReturn(true);
        store.setAddress("aaa");
        store.setPostalCode("aaaaa");
        Result<Store> result = service.update(store);
        assertFalse(result.isSuccess());
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

    private List<Store> getStoreList() {
        List<Store> stores = new ArrayList<>();
        Store a = new Store();
        Store b = new Store();

        a.setStoreId(1);
        a.setAddress("aaa");
        a.setCity("aaa");
        a.setState("AA");
        a.setPostalCode("aaaaa");
        a.setPhone("aaa-aaa-aaaa");

        b.setStoreId(2);
        b.setAddress("bbb");
        b.setCity("bbb");
        b.setState("BB");
        b.setPostalCode("bbbbb");
        b.setPhone("bbb-bbb-bbbb");

        stores.add(a);
        stores.add(b);

        return stores;

    }

}