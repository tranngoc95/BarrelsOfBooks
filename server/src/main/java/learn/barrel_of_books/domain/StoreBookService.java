package learn.barrel_of_books.domain;


import learn.barrel_of_books.data.StoreBookRepository;

import learn.barrel_of_books.models.StoreBook;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreBookService {

    private StoreBookRepository repository;

    public StoreBookService(StoreBookRepository repository) {
        this.repository = repository;
    }

    public List<StoreBook> findByBookId(int bookId) {
        return repository.findByBookId(bookId);
    }

    public Result<StoreBook> add(StoreBook storeBook) {
        Result<StoreBook> result = validate(storeBook);

        //WRITE TEST
        if(findByBookId(storeBook.getBookId()) != null) {
            for (StoreBook sb : findByBookId(storeBook.getBookId())) {
                if (storeBook.getStore().getStoreId() == sb.getStore().getStoreId()) {
                    result.addMessage("This store/book combination already exists", ResultType.INVALID);
                }
            }
        }

        if(result.isSuccess()) {
            repository.add(storeBook);
            result.setPayload(storeBook);
        }
        return result;
    }

    public Result<StoreBook> update(StoreBook storeBook) {
        Result<StoreBook> result = validate(storeBook);

        if(result.isSuccess()) {
            repository.update(storeBook);
        }
        return result;
    }

    public Result<StoreBook> delete(int store_id, int book_id) {
        Result<StoreBook> result = new Result<>();
        if(!repository.delete(store_id,book_id)) {
            result.addMessage("Store/Book combination not found", ResultType.NOT_FOUND);
        }
        return result;
    }

    private Result<StoreBook> validate(StoreBook storeBook) {
        Result<StoreBook> result = new Result<>();

        if(storeBook == null) {
            result.addMessage("StoreBook cannot be null",ResultType.INVALID);
            return result;
        }

        if (storeBook.getBookId() <= 0) {
            result.addMessage("Book ID is required", ResultType.INVALID);
        }

        if(storeBook.getStore() == null) {
            result.addMessage("Store is required",ResultType.INVALID);
        }

        return result;

    }
}
