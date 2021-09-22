package learn.barrel_of_books.controllers;

import learn.barrel_of_books.domain.GenreService;
import learn.barrel_of_books.domain.Result;
import learn.barrel_of_books.models.AppUser;
import learn.barrel_of_books.models.Genre;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000","http://127.0.0.1:5500"})
@RequestMapping("/api/genre")
public class GenreController {
    private final GenreService service;

    public GenreController(GenreService service) {
        this.service = service;
    }

    @GetMapping
    public List<Genre> findAll() {
        return service.findAll();
    }

    @GetMapping("/{genreId}")
    public Genre findById(@PathVariable int genreId) {
        return service.findById(genreId);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader("Authorization") AppUser user,
                                      @RequestBody @Valid Genre genre, BindingResult result){
        if(user == null || !user.hasRole("ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if(result.hasErrors()){
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        Result<Genre> serviceResult = service.add(genre);
        if(serviceResult.isSuccess()) {
            return new ResponseEntity<>(serviceResult.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(serviceResult);
    }

    @PutMapping("/{genreId}")
    public ResponseEntity<Object> update(@RequestHeader("Authorization") AppUser user,
                                         @RequestBody @Valid Genre genre, BindingResult result, @PathVariable int genreId){
        if(user == null || !user.hasRole("ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if(result.hasErrors() || genre==null){
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        if(genreId!=genre.getGenreId()){
            return new ResponseEntity <>(HttpStatus.CONFLICT);
        }

        Result<Genre> serviceResult = service.update(genre);
        if(serviceResult.isSuccess()) {
            return new ResponseEntity <>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(serviceResult);
    }

    @DeleteMapping("/{genreId}")
    public ResponseEntity<Genre> deleteById(@RequestHeader("Authorization") AppUser user,
                                            @PathVariable int genreId) {
        if(user == null || !user.hasRole("ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (service.deleteById(genreId)){
            return new ResponseEntity <>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity <>(HttpStatus.NOT_FOUND);
    }
}
