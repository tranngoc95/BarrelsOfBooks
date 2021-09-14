package learn.barrel_of_books.data;

import learn.barrel_of_books.models.Genre;

import java.util.List;

public interface GenreRepository {
    List<Genre> findAll();

    Genre findById(int genreId);

    Genre findByName(String name);

    Genre add(Genre genre);

    boolean update(Genre genre);

    boolean deleteById(int genreId);
}
