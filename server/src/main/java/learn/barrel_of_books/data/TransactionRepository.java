package learn.barrel_of_books.data;

import learn.barrel_of_books.models.Transaction;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TransactionRepository {
    List<Transaction> findByUserId(String userId);

    Transaction findByTransactionId(int transactionId);

    Transaction add(Transaction transaction);

    boolean update(Transaction transaction);

    @Transactional
    boolean deleteById(int transactionId);
}
