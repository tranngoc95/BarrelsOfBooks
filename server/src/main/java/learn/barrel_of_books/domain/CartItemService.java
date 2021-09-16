package learn.barrel_of_books.domain;

import learn.barrel_of_books.data.CartItemRepository;
import learn.barrel_of_books.data.TransactionRepository;
import learn.barrel_of_books.models.CartItem;
import learn.barrel_of_books.models.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService {

    private final CartItemRepository repository;
    private TransactionRepository transactionRepository;

    public CartItemService(CartItemRepository repository, TransactionRepository transactionRepository) {
        this.repository = repository;
        this.transactionRepository = transactionRepository;
    }

    public List<CartItem> findByTransactionId(int transactionId){
        return repository.findByTransactionId(transactionId);
    }

    public List<CartItem> findActiveByUserId(String userId){
        return repository.findActiveByUserId(userId);
    }

    public Result<CartItem> add(CartItem cartItem){
        Result<CartItem> result = validate(cartItem);

        if(result.isSuccess()) {
            CartItem available = repository.findActiveByUserIdAndBookId(cartItem.getUserId(),
                    cartItem.getBook().getBookId());

            if (available != null) {
                int quantity = cartItem.getQuantity() + available.getQuantity();
                available.setQuantity(quantity);
                if(!repository.update(available)){
                    String msg = String.format("cartItemId: %s, not found", cartItem.getCartItemId());
                    result.addMessage(msg, ResultType.NOT_FOUND);
                }
                result.setPayload(available);
            } else {
                if (cartItem.getCartItemId() != 0) {
                    result.addMessage("cartItemId cannot be set for `add` operation", ResultType.INVALID);
                    return result;
                }

                cartItem = repository.add(cartItem);
                result.setPayload(cartItem);
            }
        }

        return result;
    }

    public Result<CartItem> update(CartItem cartItem){
        Result<CartItem> result = validate(cartItem);

        if(result.isSuccess()) {
            CartItem available = repository.findActiveByUserIdAndBookId(cartItem.getUserId(),
                    cartItem.getBook().getBookId());

            if(available!=null && available.getCartItemId()!=cartItem.getCartItemId()){
                result.addMessage("userId and Book should not be duplicated.", ResultType.INVALID);
            }

            if(cartItem.getCartItemId()<=0){
                result.addMessage("cartItemId must be set for `update` operation", ResultType.INVALID);
            }

            if(result.isSuccess() && !repository.update(cartItem)){
                String msg = String.format("cartItemId: %s, not found", cartItem.getCartItemId());
                result.addMessage(msg, ResultType.NOT_FOUND);
            }
        }

        return result;
    }

    public boolean deleteById(int cartItemId){
        return repository.deleteById(cartItemId);
    }

    private Result<CartItem> validate(CartItem cartItem){
        Result<CartItem> result = Validate.validate(cartItem);

        if(result.isSuccess()){
            if(cartItem.getTransactionId()!=0){
                Transaction transaction = transactionRepository.findByTransactionId(cartItem.getTransactionId());
                if(!cartItem.getUserId().equals(transaction.getUserId())) {
                    result.addMessage("cart userId and transaction userId don't match.", ResultType.INVALID);
                }
            }

            if(cartItem.getQuantity() > cartItem.getBook().getQuantity()) {
                result.addMessage("cartItem quantity can't be higher than book inventory quantity.",
                        ResultType.INVALID);
            }
        }

        return result;
    }
}
