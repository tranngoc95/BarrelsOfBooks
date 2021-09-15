package learn.barrel_of_books.data;


import learn.barrel_of_books.data.mappers.BookMapper;
import learn.barrel_of_books.models.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class BookJdbcRepository implements BookRepository {

    private JdbcTemplate template;

    public BookJdbcRepository(JdbcTemplate template) {
        this.template = template;
    }


    @Override
    public List<Book> findAll() {
        final String sql = "select book_id, title, description, price, author, quantity "
                            + "from book";

       return template.query(sql,new BookMapper());
    }

    @Override
    public Book findById(int id) {
        final String sql = "select book_id, title, description, price, author, quantity "
                + "from book "
                + "where book_id = ?;";
        Book book = template.query(sql,new BookMapper(), id).stream()
                            .findFirst().orElse(null);
        if(book != null) {
            addGenres(book);
        }
        return book;
    }

    @Override
    public Book findByTitle(String title) {
        final String sql = "select book_id, title, description, price, author, quantity "
                + "from book "
                + "where title = ?;";

        Book book = template.query(sql,new BookMapper(), title).stream()
                .findFirst().orElse(null);

        if(book != null) {
            addGenres(book);
        }
        return book;
    }

    @Override
    public Book add(Book book) {
        final String sql = "insert into book(title, description, price, author, quantity) "
                            +"values (?,?,?,?,?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,book.getTitle());
            ps.setString(2,book.getDescription());
            ps.setBigDecimal(3,book.getPrice());
            ps.setString(4,book.getAuthor());
            ps.setInt(5,book.getQuantity());
            return ps;
        }, keyHolder);

        if(rowsAffected <= 0) {
            return null;
        }
        book.setBookId(keyHolder.getKey().intValue());
        return book;
    }

    @Override
    public boolean update(Book book) {
        final String sql = "update book set "
                            + "title = ?, "
                            + "description = ?, "
                            + "price = ?, "
                            + "author = ?, "
                            + "quantity = ? "
                            + "where book_id = ?;";
        return template.update(sql,book.getTitle(), book.getDescription(),
                             book.getPrice(), book.getAuthor(), book.getQuantity(), book.getBookId()) > 0;
    }

    @Override
    public boolean delete(int bookId) {
        template.update("delete from cart_item where book_id = ?;", bookId);
        template.update("delete from category_book where book_id = ?;", bookId);
        template.update("delete from store_book where book_id = ?;",bookId);
        return template.update("delete from book where book_id = ?;", bookId) > 0;
    }


    private void addGenres(Book book) {

    }
}
