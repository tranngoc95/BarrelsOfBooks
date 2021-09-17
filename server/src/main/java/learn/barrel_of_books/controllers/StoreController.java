package learn.barrel_of_books.controllers;


import learn.barrel_of_books.domain.Result;
import learn.barrel_of_books.domain.StoreService;
import learn.barrel_of_books.models.Store;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000","http://127.0.0.1:5500"})
@RequestMapping("/api/store")
public class StoreController {

    private final StoreService service;

    public StoreController(StoreService service) {
        this.service = service;
    }


    @GetMapping
    public List<Store> findAll() {
        return service.findAll();
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<Store> findById(@PathVariable int storeId) {
        Store store = service.findById(storeId);

        if(store == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(store);
    }

    @GetMapping("/postal/{postCode}")
    public ResponseEntity<Store> findById(@PathVariable String postCode) {
        Store store = service.findByPostCode(postCode);

        if(store == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(store);
    }


    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Store store) {
        Result<Store> result = service.add(store);

        if(result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
     }


     @PutMapping("/{storeId}")
    public ResponseEntity<Object> update(@PathVariable int storeId, @RequestBody Store store) {
        if(store.getStoreId() != storeId) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Result<Store> result = service.update(store);
        if(result.isSuccess()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
     }

     @DeleteMapping("/{storeId}")
    public ResponseEntity<Store> delete(@PathVariable int storeId) {
        Result<Store> result = service.delete(storeId);

        if(result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
     }



}
