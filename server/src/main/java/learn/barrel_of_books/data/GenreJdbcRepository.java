package learn.barrel_of_books.data;

import learn.barrel_of_books.data.mappers.GenreMapper;
import learn.barrel_of_books.models.Genre;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class GenreJdbcRepository implements GenreRepository {
    private JdbcTemplate jdbcTemplate;

    public GenreJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> findAll(){
        final String sql = "select genre_id, name, description from genre";
        return jdbcTemplate.query(sql, new GenreMapper());
    }

    @Override
    public Genre findById(int genreId){
        final String sql = "select genre_id, name, description " +
                "from genre where genre_id = ?";
        return jdbcTemplate.query(sql, new GenreMapper(), genreId)
                .stream().findFirst().orElse(null);
    }

    @Override
    public Genre findByName(String name){
        final String sql = "select genre_id, name, description " +
                "from genre where name = ?";
        return jdbcTemplate.query(sql, new GenreMapper(), name)
                .stream().findFirst().orElse(null);
    }

    @Override
    public Genre add(Genre genre){
        final String sql = "insert into genre (name, description) values (?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, genre.getName());
            ps.setString(2, genre.getDescription());
            return ps;
        }, keyHolder);

        if(rowAffected <=0) {
            return null;
        }
        genre.setGenreId(keyHolder.getKey().intValue());
        return genre;
    }

    @Override
    public boolean update(Genre genre) {
        final String sql = "update genre set name = ?, description = ? where genre_id = ?";
        return jdbcTemplate.update(sql, genre.getName(), genre.getDescription(), genre.getGenreId())>0;
    }

    @Override
    public boolean deleteById(int genreId) {
        final String sql = "delete from genre where genre_id = ?";
        return jdbcTemplate.update(sql, genreId)>0;
    }
}
