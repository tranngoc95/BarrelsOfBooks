package learn.barrel_of_books.controllers;

import learn.barrel_of_books.domain.BookService;
import learn.barrel_of_books.domain.Result;
import learn.barrel_of_books.models.Book;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }


    @GetMapping
    public List<Book> findAll() {
        return service.findAll();
    }


    @GetMapping("/{bookId}")
    public ResponseEntity<Book> findById(@PathVariable int bookId) {
        Book book = service.findById(bookId);

        if(book == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(book);

    }

    @GetMapping("/title/{title}")
    public ResponseEntity<Book> findByTitle(@PathVariable String title) {
        Book book = service.findByTitle(title);

        if(book == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(book);

    }


    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Book book) {
        Result<Book> result = service.add(book);

        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);

    }


    @PutMapping("/{bookId}")
    public ResponseEntity<Object> update(@PathVariable int bookId, @RequestBody Book book) {
        if(book.getBookId() != bookId) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Book> result = service.update(book);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }


    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> delete(@PathVariable int bookId) {
        Result<Book> result = service.delete(bookId);
        if(result.isSuccess()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



}
