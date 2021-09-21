package learn.barrel_of_books.controllers;


import learn.barrel_of_books.domain.Result;
import learn.barrel_of_books.domain.StoreBookService;
import learn.barrel_of_books.models.AppUser;
import learn.barrel_of_books.models.StoreBook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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
    public ResponseEntity<Object> add(@RequestHeader("Authorization") AppUser user,
                                      @RequestBody StoreBook storeBook) {
        if(user == null || !user.hasRole("MANAGER")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Result<StoreBook> result = service.add(storeBook);

        if(result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }


    @PutMapping("/{bookId}/{storeId}")
    public ResponseEntity<Object> update(@RequestHeader("Authorization") AppUser user,
                                         @PathVariable int bookId, @PathVariable int storeId, @RequestBody StoreBook storeBook) {
        if(user == null || !user.hasRole("MANAGER")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if(bookId != storeBook.getBookId() || storeBook.getStore().getStoreId() != storeId) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<StoreBook> result = service.update(storeBook);
        if(result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }


    @DeleteMapping("/{bookId}/{storeId}")
    public ResponseEntity<Void> delete(@RequestHeader("Authorization") AppUser user,
                                       @PathVariable int bookId, @PathVariable int storeId) {
        if(user == null || !user.hasRole("MANAGER")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        
        Result<StoreBook> result = service.delete(storeId,bookId);

        if(result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
