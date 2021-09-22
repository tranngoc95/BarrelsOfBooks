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

    @GetMapping("/{bookId}/{state}")
    public List<StoreBook> findByBookIdAndState(@PathVariable("bookId") int bookId, @PathVariable("state") String state) {
        return service.findByBookIdAndState(bookId, state);
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


    @DeleteMapping("/{bookId}")
    public ResponseEntity<Object> delete(@RequestHeader("Authorization") AppUser user,
                                       @PathVariable int bookId) {
        if(user == null || !user.hasRole("MANAGER")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        
        Result<StoreBook> result = service.delete(bookId);

        if(result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }

}
