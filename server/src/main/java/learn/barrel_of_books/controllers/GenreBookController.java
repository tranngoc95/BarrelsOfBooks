package learn.barrel_of_books.controllers;

import learn.barrel_of_books.domain.GenreBookService;
import learn.barrel_of_books.domain.Result;
import learn.barrel_of_books.models.AppUser;
import learn.barrel_of_books.models.GenreBook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genre-book")
public class GenreBookController {

    private final GenreBookService service;

    public GenreBookController(GenreBookService service) {
        this.service = service;
    }

    @GetMapping("/{bookId}")
    public List<GenreBook> findByBookId(@PathVariable int bookId) {
        return service.findByBookId(bookId);
    }


    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader("Authorization") AppUser user,
                                      @RequestBody GenreBook genreBook) {

        if(user == null || !user.hasRole("MANAGER")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Result<GenreBook> result = service.add(genreBook);

        if(result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{bookId}/{genreId}")
    public ResponseEntity<Void> delete(@RequestHeader("Authorization") AppUser user,
                                       @PathVariable int bookId, @PathVariable int genreId) {

        if(user == null || !user.hasRole("MANAGER")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Result<GenreBook> result = service.delete(bookId,genreId);

        if(result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
