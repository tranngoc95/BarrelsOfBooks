package learn.barrel_of_books.domain;

import learn.barrel_of_books.data.BookRepository;
import learn.barrel_of_books.data.CartItemRepository;
import learn.barrel_of_books.data.TransactionRepository;
import learn.barrel_of_books.models.Book;
import learn.barrel_of_books.models.CartItem;
import learn.barrel_of_books.models.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionService {

    private TransactionRepository repository;
    private CartItemRepository cartItemRepository;
    private BookRepository bookRepository;

    public TransactionService(TransactionRepository repository, CartItemRepository cartItemRepository, BookRepository bookRepository) {
        this.repository = repository;
        this.cartItemRepository = cartItemRepository;
        this.bookRepository = bookRepository;
    }

    public List<Transaction> findByUserId(String userId) {
        return repository.findByUserId(userId);
    }

    public Transaction findByTransactionId(int transactionId){
        return repository.findByTransactionId(transactionId);
    }

    @Transactional
    public Result<Transaction> add(Transaction transaction){
        Result<Transaction> result = validate(transaction);

        if(result.isSuccess()){
            if (transaction.getTransactionId() != 0) {
                result.addMessage("transactionId cannot be set for `add` operation", ResultType.INVALID);
                return result;
            }

            transaction.updateTotal();
            transaction = repository.add(transaction);

            for(int i=0; i<transaction.getBooks().size(); i++){
                CartItem each = transaction.getBooks().get(i);
                if(!each.equals(cartItemRepository.findByCartItemId(each.getCartItemId()))){
                    result.addMessage("cartItem doesn't match inventory.", ResultType.INVALID);
                    return result;
                }

                each.setTransactionId(transaction.getTransactionId());
                cartItemRepository.update(each);
                each.getBook().subtractQuantity(each.getQuantity());
                bookRepository.update(each.getBook());
            }
            result.setPayload(transaction);
        }
        return null;
    }

//    public Result<Transaction> update(Transaction transaction) {
//        Result<Transaction> result = validate(transaction);
//
//        if(result.isSuccess()){
//            if(transaction.getTransactionId()<=0){
//                result.addMessage("transactionId must be set for `update` operation", ResultType.INVALID);
//            }
//
//            if(result.isSuccess() && !repository.update(transaction)){
//                String msg = String.format("transactionId: %s, not found", transaction.getTransactionId());
//                result.addMessage(msg, ResultType.NOT_FOUND);
//            }
//        }
//        return result;
//    }

    public boolean deleteById(int transactionId){
        return repository.deleteById(transactionId);
    }

    private Result<Transaction> validate(Transaction transaction){
        Result<Transaction> result = Validate.validate(transaction);

        if(result.isSuccess()){
            for(CartItem each: transaction.getBooks()) {
                if(!each.getUserId().equals(transaction.getUserId())){
                    result.addMessage("cart userId and transaction userId don't match.", ResultType.INVALID);
                    break;
                }

                if(each.getQuantity() > each.getBook().getQuantity()) {
                    result.addMessage("cartItem quantity can't be higher than book inventory quantity.",
                            ResultType.INVALID);
                }
            }
        }

        return result;
    }

}
