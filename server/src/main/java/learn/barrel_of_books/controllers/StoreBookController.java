package learn.barrel_of_books.controllers;


import learn.barrel_of_books.domain.Result;
import learn.barrel_of_books.domain.StoreBookService;
import learn.barrel_of_books.models.StoreBook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000","http://127.0.0.1:5500"})
@RequestMapping("/api/store-book")
public class StoreBookController {

    private final StoreBookService service;

    public StoreBookController(StoreBookService service) {
        this.service = service;
    }

    @GetMapping("/{bookId}")
    public List<StoreBook> findByBookId(@PathVariable int bookId) {
       return service.findByBookId(bookId);
    }

    @PostMapping()
    public ResponseEntity<Object> add(@RequestBody StoreBook storeBook) {
        Result<StoreBook> result = service.add(storeBook);

        if(result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }


    @PutMapping("/{bookId}/{storeId}")
    public ResponseEntity<Object> update(@PathVariable int bookId, @PathVariable int storeId, @RequestBody StoreBook storeBook) {
        if(bookId != storeBook.getBookId() || storeBook.getStore().getStoreId() != storeId) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<StoreBook> result = service.update(storeBook);
        if(result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }


    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> delete(@PathVariable int bookId) {
        Result<StoreBook> result = service.delete(bookId);

        if(result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
