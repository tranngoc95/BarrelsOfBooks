package learn.barrel_of_books.data.mappers;


import learn.barrel_of_books.models.GenreBook;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreBookMapper implements RowMapper<GenreBook> {

    public GenreBook mapRow(ResultSet resultSet, int i) throws SQLException {
        GenreBook genreBook = new GenreBook();
        genreBook.setBookId(resultSet.getInt("book_id"));


        GenreMapper genreMapper = new GenreMapper();
        genreBook.setGenre(genreMapper.mapRow(resultSet, i));

        return genreBook;
    }

}