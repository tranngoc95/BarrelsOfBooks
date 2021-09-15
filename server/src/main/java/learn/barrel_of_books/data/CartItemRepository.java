package learn.barrel_of_books.data;

import learn.barrel_of_books.models.CartItem;

import java.util.List;

public interface CartItemRepository {
    List<CartItem> findByTransactionId(int transactionId);

    List<CartItem> findActiveByUserId(String userId);

    public CartItem findActiveByUserIdAndBookId(String userId, int bookId);

    CartItem add(CartItem cartItem);

    boolean update(CartItem cartItem);

    boolean deleteById(int cartItemId);
}
