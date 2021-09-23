package learn.barrel_of_books.data;

import learn.barrel_of_books.data.mappers.CartItemMapper;
import learn.barrel_of_books.models.CartItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

@Repository
public class CartItemJdbcRepository implements CartItemRepository {
    private JdbcTemplate jdbcTemplate;

    public CartItemJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<CartItem> findActiveByUserId(String userId) {
        final String sql = "select c.cart_item_id, c.transaction_id, c.user_id, c.quantity item_quantity, c.book_id, " +
                "b.quantity, b.title, b.description, b.author, b.price, " +
                "b.publisher, b.language, b.pages, b.age_range, b.dimensions, b.isbn13 from cart_item c " +
                "left outer join book b on b.book_id = c.book_id where c.user_id = ? and c.transaction_id is null";
        return jdbcTemplate.query(sql, new CartItemMapper(), userId);
    }

    @Override
    public CartItem findByCartItemId(int cartItemId) {
        final String sql = "select c.cart_item_id, c.transaction_id, c.user_id, c.quantity item_quantity, c.book_id, " +
                "b.quantity, b.title, b.description, b.author, b.price, " +
                "b.publisher, b.language, b.pages, b.age_range, b.dimensions, b.isbn13 from cart_item c " +
                "left outer join book b on b.book_id = c.book_id where c.cart_item_id = ?";
        return jdbcTemplate.query(sql, new CartItemMapper(), cartItemId).stream().findFirst().orElse(null);
    }

    @Override
    public CartItem findActiveByUserIdAndBookId(String userId, int bookId){
        final String sql = "select c.cart_item_id, c.transaction_id, c.user_id, c.quantity item_quantity, c.book_id, " +
                "b.quantity, b.title, b.description, b.author, b.price, " +
                "b.publisher, b.language, b.pages, b.age_range, b.dimensions, b.isbn13 from cart_item c " +
                "left outer join book b on b.book_id = c.book_id " +
                "where c.user_id = ? and c.book_id = ? and c.transaction_id is null";
        return jdbcTemplate.query(sql, new CartItemMapper(), userId, bookId).stream().findFirst().orElse(null);

    }

    @Override
    public CartItem add(CartItem cartItem) {
        final String sql = "insert into cart_item (user_id, book_id, quantity, transaction_id) values (?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, cartItem.getUserId());
            ps.setInt(2, cartItem.getBook().getBookId());
            ps.setInt(3, cartItem.getQuantity());
            if(cartItem.getTransactionId()>0) {
                ps.setInt(4, cartItem.getTransactionId());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            return ps;
        }, keyHolder);

        if(rowAffected <= 0) {
            return null;
        }

        cartItem.setCartItemId(keyHolder.getKey().intValue());
        return cartItem;
    }

    @Override
    public boolean update(CartItem cartItem) {
        final String sql = "update cart_item set user_id = ?, book_id = ?, " +
                "quantity = ?, transaction_id = ? where cart_item_id = ?";

        return jdbcTemplate.update(sql, cartItem.getUserId(), cartItem.getBook().getBookId(),
                cartItem.getQuantity(), cartItem.getTransactionId() > 0 ? cartItem.getTransactionId() : null,
                cartItem.getCartItemId()) > 0;
    }

    @Override
    public boolean deleteById(int cartItemId) {
        return jdbcTemplate.update("delete from cart_item where cart_item_id = ?", cartItemId) > 0;
    }
}
