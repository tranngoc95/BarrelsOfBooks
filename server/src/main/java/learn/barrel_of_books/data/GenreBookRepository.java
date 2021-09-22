package learn.barrel_of_books.data;

import learn.barrel_of_books.models.GenreBook;

import java.util.List;

public interface GenreBookRepository {
    List<GenreBook> findByBookId(int bookId);

    boolean add(GenreBook genreBook);

    boolean delete(int bookId);
}
