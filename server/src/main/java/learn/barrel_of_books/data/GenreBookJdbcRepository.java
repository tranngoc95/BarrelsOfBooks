package learn.barrel_of_books.data;


import learn.barrel_of_books.data.mappers.GenreBookMapper;

import learn.barrel_of_books.models.GenreBook;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GenreBookJdbcRepository implements GenreBookRepository {

    private JdbcTemplate template;

    public GenreBookJdbcRepository(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public List<GenreBook> findByBookId(int bookId) {
        final String sql = "select gb.book_id, gb.genre_id, g.name, g.description "
                + "from genre_book gb "
                + "left outer join genre g on g.genre_id = gb.genre_id "
                + "where gb.book_id = ?;";
        GenreBook gb =  template.query(sql, new GenreBookMapper(), bookId).stream()
                .findFirst().orElse(null);

        if(gb == null) {
            return null;
        }
        return template.query(sql,new GenreBookMapper(),bookId);
    }

    @Override
    public boolean add(GenreBook genreBook) {
        final String sql = "insert into genre_book (book_id, genre_id) "
                            + "values (?,?);";
        return template.update(sql,genreBook.getBookId(),genreBook.getGenre().getGenreId()) > 0;
    }


    @Override
    public boolean delete(int bookId) {
        final String sql = "delete from genre_book "
                            + "where book_id = ?; ";
        return template.update(sql,bookId) > 0;
    }

}
