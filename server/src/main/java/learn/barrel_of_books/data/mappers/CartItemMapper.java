package learn.barrel_of_books.data.mappers;

import learn.barrel_of_books.models.CartItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CartItemMapper implements RowMapper<CartItem> {
    @Override
    public CartItem mapRow(ResultSet resultSet, int i) throws SQLException {
        CartItem cartItem = new CartItem();
        cartItem.setCartItemId(resultSet.getInt("cart_item_id"));
        cartItem.setTransactionId(resultSet.getInt("transaction_id"));
        cartItem.setUserId(resultSet.getString("user_id"));
        cartItem.setQuantity(resultSet.getInt("item_quantity"));

        BookMapper bookMapper = new BookMapper();
        cartItem.setBook(bookMapper.mapRow(resultSet,i));

        return cartItem;
    }
}
