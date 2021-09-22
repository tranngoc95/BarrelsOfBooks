package learn.barrel_of_books.data;

import learn.barrel_of_books.models.StoreBook;

import java.util.List;

public interface StoreBookRepository {
    List<StoreBook> findByBookId(int bookId);

    List<StoreBook> findByBookIdAndState(int bookId, String state);

    boolean add(StoreBook storeBook);

    boolean update(StoreBook storeBook);

    boolean delete(int book_id);
}
