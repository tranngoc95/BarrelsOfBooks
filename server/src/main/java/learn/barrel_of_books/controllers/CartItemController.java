package learn.barrel_of_books.controllers;

import learn.barrel_of_books.domain.CartItemService;
import learn.barrel_of_books.domain.Result;
import learn.barrel_of_books.models.AppUser;
import learn.barrel_of_books.models.Cart;
import learn.barrel_of_books.models.CartItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/cart-item")
public class CartItemController {
    private final CartItemService service;

    public CartItemController(CartItemService service) {
        this.service = service;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Cart> findCartActiveByUserId(@RequestHeader("Authorization") AppUser user,
                                       @PathVariable String userId){

        if(user == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(service.findCartActiveByUserId(userId));
    }

    @GetMapping("/{cartItemId}")
    public ResponseEntity<CartItem> findByCartItemId(@RequestHeader("Authorization") AppUser user,
                                                     @PathVariable int cartItemId){

        if(user == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        CartItem cartItem = service.findByCartItemId(cartItemId);
        if (cartItem == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(cartItem);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader("Authorization") AppUser user,
                                      @RequestBody @Valid CartItem cartItem, BindingResult result){
        if(user == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

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
    public ResponseEntity<Object> update(@RequestHeader("Authorization") AppUser user,
                                         @RequestBody @Valid CartItem cartItem, BindingResult result, @PathVariable int cartItemId){
        if(user == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

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
    public ResponseEntity<Object> deleteById(@RequestHeader("Authorization") AppUser user,
                                             @PathVariable int cartItemId) {
        if(user == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Result<CartItem> serviceResult = service.deleteById(cartItemId);
        if(serviceResult.isSuccess()) {
            return new ResponseEntity <>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(serviceResult);
    }
}
