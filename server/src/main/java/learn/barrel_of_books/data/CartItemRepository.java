package learn.barrel_of_books.data;

import learn.barrel_of_books.models.CartItem;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartItemRepository {
    List<CartItem> findByTransactionId(int transactionId);

    List<CartItem> findActiveByUserId(String userId);

    CartItem findActiveByUserIdAndBookId(String userId, int bookId);

    CartItem findByCartItemId(int cartItemId);

    CartItem add(CartItem cartItem);

    boolean update(CartItem cartItem);

    @Transactional
    boolean deleteById(int cartItemId);
}
