package learn.barrel_of_books.controllers;

import learn.barrel_of_books.domain.CartItemService;
import learn.barrel_of_books.domain.Result;
import learn.barrel_of_books.models.CartItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/cart-item")
public class CartItemController {
    private final CartItemService service;

    public CartItemController(CartItemService service) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    public List<CartItem> findActiveByUserId(@PathVariable String userId){
        return service.findActiveByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody @Valid CartItem cartItem, BindingResult result){
        if(result.hasErrors()){
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        Result<CartItem> serviceResult = service.add(cartItem);
        if(serviceResult.isSuccess()) {
            return new ResponseEntity<>(serviceResult.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(serviceResult);
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<Object> update(@RequestBody @Valid CartItem cartItem, BindingResult result, @PathVariable int cartItemId){
        if(result.hasErrors() || cartItem==null){
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        if(cartItemId!=cartItem.getCartItemId()){
            return new ResponseEntity <>(HttpStatus.CONFLICT);
        }

        Result<CartItem> serviceResult = service.update(cartItem);
        if(serviceResult.isSuccess()) {
            return new ResponseEntity <>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(serviceResult);
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Object> deleteById(@PathVariable int cartItemId) {
        Result<CartItem> serviceResult = service.deleteById(cartItemId);
        if(serviceResult.isSuccess()) {
            return new ResponseEntity <>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(serviceResult);
    }
}
