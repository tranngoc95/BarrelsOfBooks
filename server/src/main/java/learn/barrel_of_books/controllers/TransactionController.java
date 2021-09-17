package learn.barrel_of_books.controllers;

import learn.barrel_of_books.domain.Result;
import learn.barrel_of_books.domain.TransactionService;
import learn.barrel_of_books.models.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping("/user/{userId}")
    public List<Transaction> findByUserId(@PathVariable String userId) {
        return service.findByUserId(userId);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> findByTransactionId(@PathVariable int transactionId){
        Transaction transaction = service.findByTransactionId(transactionId);
        if (transaction == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(transaction);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody @Valid Transaction transaction, BindingResult result){
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
    public ResponseEntity<Object> update(@RequestBody @Valid Transaction transaction, BindingResult result,
                                         @PathVariable int transactionId){
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
    public ResponseEntity<Transaction> deleteById(@PathVariable int transactionId) {
        if (service.deleteById(transactionId)){
            return new ResponseEntity <>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity <>(HttpStatus.NOT_FOUND);
    }
}
