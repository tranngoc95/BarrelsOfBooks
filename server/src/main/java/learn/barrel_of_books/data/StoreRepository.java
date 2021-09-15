package learn.barrel_of_books.data;

import learn.barrel_of_books.models.Store;

import java.util.List;

public interface StoreRepository {
    List<Store> findAll();

    Store findById(int id);

    Store findByPostCode(String postCode);

    Store add(Store store);

    boolean update(Store store);

    boolean delete(int storeId);
}
