package learn.barrel_of_books.data.mappers;

import learn.barrel_of_books.models.Book;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookMapper implements RowMapper<Book> {

    @Override
    public Book mapRow(ResultSet resultSet, int i) throws SQLException {
        Book book = new Book();
        book.setBookId(resultSet.getInt("book_id"));
        book.setTitle(resultSet.getString("title"));
        book.setDescription(resultSet.getString("description"));
        book.setPrice(resultSet.getBigDecimal("price"));
        book.setAuthor(resultSet.getString("author"));
        book.setQuantity(resultSet.getInt("quantity"));
        book.setPublisher(resultSet.getString("publisher"));
        book.setLanguage(resultSet.getString("language"));
        book.setPages(resultSet.getInt("pages"));
        book.setAgeRange(resultSet.getString("age_range"));
        book.setDimensions(resultSet.getString("dimensions"));
        book.setIsbn13(resultSet.getString("isbn13"));
        return book;
    }
}