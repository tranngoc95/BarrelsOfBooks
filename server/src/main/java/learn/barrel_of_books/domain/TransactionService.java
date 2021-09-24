package learn.barrel_of_books.domain;

import learn.barrel_of_books.data.BookRepository;
import learn.barrel_of_books.data.CartItemRepository;
import learn.barrel_of_books.data.TransactionRepository;
import learn.barrel_of_books.models.Book;
import learn.barrel_of_books.models.CartItem;
import learn.barrel_of_books.models.Transaction;
import learn.barrel_of_books.models.TransactionStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    public List<Transaction> findAll() {
        return repository.findAll();
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

            if(!transaction.getTotal().equals(BigDecimal.ZERO)){
                result.addMessage("Total cannot be preset for `add` operation", ResultType.INVALID);
            }

            for(CartItem each: transaction.getBooks()) {
                if(each.getQuantity() > each.getBook().getQuantity()) {
                    result.addMessage("cartItem quantity can't be higher than book inventory quantity.",
                            ResultType.INVALID);
                    return result;
                }

                CartItem other = cartItemRepository.findByCartItemId(each.getCartItemId());
                if (!each.equals(other)) {
                    result.addMessage("cartItem doesn't match inventory.", ResultType.INVALID);
                    return result;
                }
            }

            transaction.updateTotal();
            if(transaction.getDate()==null){
                transaction.setDate(LocalDate.now());
            }

            transaction = repository.add(transaction);

            for(int i=0; i<transaction.getBooks().size(); i++){
                CartItem each = transaction.getBooks().get(i);
                each.setTransactionId(transaction.getTransactionId());
                cartItemRepository.update(each);
                each.getBook().subtractQuantity(each.getQuantity());
                bookRepository.update(each.getBook());
            }
            result.setPayload(transaction);
        }

        return result;
    }

    public Result<Transaction> update(Transaction transaction) {
        Result<Transaction> result = validate(transaction);

        if(result.isSuccess()){
            if(transaction.getTransactionId()<=0){
                result.addMessage("transactionId must be set for `update` operation", ResultType.INVALID);
                return result;
            }

            if(transaction.getDate()==null) {
                result.addMessage("Date cannot be null.", ResultType.INVALID);
            }

            Transaction oldTransaction = repository.findByTransactionId(transaction.getTransactionId());
            if(oldTransaction == null) {
                String msg = String.format("transactionId: %s, not found", transaction.getTransactionId());
                result.addMessage(msg, ResultType.NOT_FOUND);
            } else if(!transaction.getBooks().equals(oldTransaction.getBooks())){
                result.addMessage("Transaction books cannot be updated.", ResultType.INVALID);
            }

            if(result.isSuccess()) {
                repository.update(transaction);
            }
        }
        return result;
    }

    @Transactional
    public Result<Transaction> deleteById(int transactionId){
        Result<Transaction> result = new Result<>();
        Transaction transaction = findByTransactionId(transactionId);

        if(transaction==null){
            String msg = String.format("transactionId: %s, not found", transactionId);
            result.addMessage(msg, ResultType.NOT_FOUND);
            return result;
        }

        if(transaction.getStatus()!= TransactionStatus.ORDERED){
            result.addMessage("Shipped or delivered ordered cannot be canceled.", ResultType.INVALID);
            return result;
        }

        for(CartItem each : transaction.getBooks()){
            Book book = each.getBook();
            book.addQuantity(each.getQuantity());
            bookRepository.update(book);
        }

        repository.deleteById(transactionId);

        return result;
    }

    private Result<Transaction> validate(Transaction transaction){
        Result<Transaction> result = Validate.validate(transaction);

        if(result.isSuccess()){
            for(CartItem each: transaction.getBooks()) {
                if(!each.getUserId().equals(transaction.getUserId())){
                    result.addMessage("cart userId and transaction userId don't match.", ResultType.INVALID);
                    break;
                }

                if(transaction.getDate()!=null && transaction.getDate().isAfter(LocalDate.now())) {
                    result.addMessage("Transaction date can't be in future.",
                            ResultType.INVALID);
                }
            }
        }

        return result;
    }

}
