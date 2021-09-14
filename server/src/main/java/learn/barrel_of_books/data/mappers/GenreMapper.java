package learn.barrel_of_books.data.mappers;

import learn.barrel_of_books.models.Genre;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreMapper implements RowMapper<Genre> {

    @Override
    public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
        Genre genre = new Genre();
        genre.setGenreId(resultSet.getInt("genre_id"));
        genre.setName(resultSet.getString("name"));
        genre.setDescription(resultSet.getString("description"));
        return genre;
    }


}
