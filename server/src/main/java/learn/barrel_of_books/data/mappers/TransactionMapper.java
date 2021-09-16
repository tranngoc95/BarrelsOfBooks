package learn.barrel_of_books.data.mappers;

import learn.barrel_of_books.models.Transaction;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionMapper implements RowMapper<Transaction> {
    @Override
    public Transaction mapRow(ResultSet resultSet, int i) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(resultSet.getInt("transaction_id"));
        transaction.setDate(resultSet.getDate("date").toLocalDate());
        transaction.setUserId(resultSet.getString("user_id"));
        transaction.setTotal(resultSet.getBigDecimal("total"));
        transaction.setEmployeeDiscount(resultSet.getBoolean("employee_discount"));
        return transaction;
    }
}
