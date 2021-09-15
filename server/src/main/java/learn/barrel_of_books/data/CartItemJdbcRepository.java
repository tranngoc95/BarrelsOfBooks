package learn.barrel_of_books.data;

import learn.barrel_of_books.data.mappers.CartItemMapper;
import learn.barrel_of_books.data.mappers.GenreMapper;
import learn.barrel_of_books.models.CartItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CartItemJdbcRepository {
    private JdbcTemplate jdbcTemplate;

    public CartItemJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<CartItem> findByTransactionId(){
        final String sql = "select cart_item_id, transaction_id, user_id, quantity, is_purchased from cart_item";
        return jdbcTemplate.query(sql, new CartItemMapper());
    }

    public List<CartItem> findActiveByUserId() {
        return null;
    }

    public CartItem add() {
        return null;
    }

    public boolean update() {
        return false;
    }

    public boolean deleteById() {
        return false;
    }
}
