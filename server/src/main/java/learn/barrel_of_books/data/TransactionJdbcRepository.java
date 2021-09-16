package learn.barrel_of_books.data;

import learn.barrel_of_books.data.mappers.CartItemMapper;
import learn.barrel_of_books.data.mappers.TransactionMapper;
import learn.barrel_of_books.models.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class TransactionJdbcRepository implements TransactionRepository {

    private JdbcTemplate jdbcTemplate;

    public TransactionJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transaction> findByUserId(String userId) {
        final String sql = "select transaction_id, user_id, date, total, employee_discount, status " +
                "from transaction where user_id = ?";
        return jdbcTemplate.query(sql, new TransactionMapper(), userId);
    }

    @Override
    public Transaction findByTransactionId(int transactionId){
        final String sql = "select transaction_id, user_id, date, total, employee_discount, status " +
                "from transaction where transaction_id = ?";
        Transaction result = jdbcTemplate.query(sql, new TransactionMapper(),
                transactionId).stream().findFirst().orElse(null);

        if (result != null) {
            addBooks(result);
        }

        return result;
    }

    @Override
    public Transaction add(Transaction transaction){
        final String sql = "insert into transaction (user_id, date, total, employee_discount, status) values (?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, transaction.getUserId());
            ps.setDate(2, Date.valueOf(transaction.getDate()));
            ps.setBigDecimal(3, transaction.getTotal());
            ps.setBoolean(4, transaction.isEmployeeDiscount());
            ps.setString(5,transaction.getStatus().toString());
            return ps;
        }, keyHolder);

        if(rowAffected <= 0) {
            return null;
        }

        transaction.setTransactionId(keyHolder.getKey().intValue());
        return transaction;
    }


    @Override
    public boolean update(Transaction transaction) {
        final String sql = "update transaction set user_id = ?, date = ?, " +
                "total = ?, employee_discount = ?, status = ? where transaction_id = ?";

        return jdbcTemplate.update(sql, transaction.getUserId(), transaction.getDate(),
                transaction.getTotal(), transaction.isEmployeeDiscount(),
                transaction.getStatus().toString(), transaction.getTransactionId()) > 0;
    }

    @Override
    @Transactional
    public boolean deleteById(int transactionId) {
        jdbcTemplate.update("delete from cart_item where transaction_id = ?", transactionId);
        return jdbcTemplate.update("delete from transaction where transaction_id = ?", transactionId) > 0;
    }

    private void addBooks(Transaction transaction) {
        final String sql = "select c.cart_item_id, c.transaction_id, c.user_id, c.quantity item_quantity, c.book_id, " +
                "b.quantity, b.title, b.description, b.author, b.price from cart_item c " +
                "left outer join book b on b.book_id = c.book_id where c.transaction_id = ?";
        var books = jdbcTemplate.query(sql, new CartItemMapper(), transaction.getTransactionId());
        transaction.setBooks(books);
    }
}
