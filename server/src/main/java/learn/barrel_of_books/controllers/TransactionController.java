package learn.barrel_of_books.controllers;

import learn.barrel_of_books.domain.Result;
import learn.barrel_of_books.domain.TransactionService;
import learn.barrel_of_books.models.AppUser;
import learn.barrel_of_books.models.CartItem;
import learn.barrel_of_books.models.Transaction;
import learn.barrel_of_books.utility.JwtConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    JwtConverter jwtConverter;

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> findAll(@RequestHeader("Authorization") AppUser user) {

        if(user == null || !user.hasRole("MANAGER")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Transaction>> findByUserId(@RequestHeader("Authorization") AppUser user,
                                                          @PathVariable String userId) {
        if(user == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(service.findByUserId(userId));
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> findByTransactionId(@RequestHeader("Authorization") AppUser user,
                                                           @PathVariable int transactionId){
        if(user == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Transaction transaction = service.findByTransactionId(transactionId);
        if (transaction == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(transaction);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader("Authorization") AppUser user,
                                      @RequestBody @Valid Transaction transaction, BindingResult result){
        if(user == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if(result.hasErrors()){
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        Result<Transaction> serviceResult = service.add(transaction);
        if(serviceResult.isSuccess()) {
            return new ResponseEntity<>(serviceResult.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(serviceResult);
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<Object> update(@RequestHeader("Authorization") AppUser user,
                                         @RequestBody @Valid Transaction transaction, BindingResult result,
                                         @PathVariable int transactionId){
        if(user == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if(result.hasErrors() || transaction==null){
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        if(transactionId!=transaction.getTransactionId()){
            return new ResponseEntity <>(HttpStatus.CONFLICT);
        }

        Result<Transaction> serviceResult = service.update(transaction);
        if(serviceResult.isSuccess()) {
            return new ResponseEntity <>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(serviceResult);
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Object> deleteById(@RequestHeader("Authorization") AppUser user,
                                                  @PathVariable int transactionId) {
        if(user == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Result<Transaction> serviceResult = service.deleteById(transactionId);
        if (serviceResult.isSuccess()){
            return new ResponseEntity <>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(serviceResult);
    }
}
