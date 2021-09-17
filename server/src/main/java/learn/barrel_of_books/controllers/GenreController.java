package learn.barrel_of_books.controllers;

import learn.barrel_of_books.domain.GenreService;
import learn.barrel_of_books.domain.Result;
import learn.barrel_of_books.models.Genre;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
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
    public ResponseEntity<Object> add(@RequestBody @Valid Genre genre, BindingResult result){
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
    public ResponseEntity<Object> update(@RequestBody @Valid Genre genre, BindingResult result, @PathVariable int genreId){
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
    public ResponseEntity<Genre> deleteById(@PathVariable int genreId) {
        if (service.deleteById(genreId)){
            return new ResponseEntity <>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity <>(HttpStatus.NOT_FOUND);
    }
}
